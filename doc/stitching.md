# Stitching

## Problem

Kosha scrapes data from Sangeethapriya, Karnatik and Wikipedia. All three sources have different schema, and have to be _stitched_ into a mediated schema. 

These three sources of data are [semantically heteregenous](https://en.wikipedia.org/wiki/Semantic_heterogeneity), i.e. there are three different schemas developed by independent parties for the same domain, resulting in differences in meaning and interpretation of data values.

**E.g.**: the ragam _Gangeyabhushani_ is spelt in the Karnatik dataset as _gangayabhooshhani_ and in the Sangeethapriya dataset as _gAngEyabhUshaNi_.

To map these different values to a single semantic category, we must [semantically translate](https://en.wikipedia.org/wiki/Semantic_translation) values to match and establish equivalence of **instances** (e.g. _gangayabhooshhani_ ≡  _gAngEyabhUshaNi_, in the above case). Class equivalence (e.g. _tracks_ ≡ _renditions_) and attribute equivalence (e.g. _raga\_name_ ≡ _raga_) can be established manually because there are only a few such terms. 

However, matching of instances must be done programmatically as they are higher in number (or crowdsourced).


## Solution

The book _[Mining of Massive Datasets](http://infolab.stanford.edu/~ullman/mmds/book.pdf)_ by _A. Rajaraman and Jeff Ullman_ identifies this problem as **'Entity Resolution'** _(pg. 111)_. The solution consists of two parts:

1. Define a string metric to assign a similarity score to each pair of strings (e.g. raga names).
2. Use the [Locality-Sensitive Hashing (LSH)](https://en.wikipedia.org/wiki/Locality-sensitive_hashing) scheme: classify string pairs into buckets, and apply different distance metrics on each bucket. These distance metrics may also include contextual information (e.g. how many kritis of raga A and B are similar). 

Here, we document the LSH approach and catalogue the various string distance metrics we could experiment with.

### 1. Locality-Sensitive Hashing (LSH)

Steps involved:

1. We use our distance metric on each pair of strings to assign a probability indicating that they are semantically equivalent.
2. We classify these string pairs into buckets based on the probability range they are in. 
3. For each bucket, we apply different types of distance metrics to the string pairs in it. These different distance metrics form a _family of Locally-Sensitive Functions_. We can 'amplify' this family of distance metrics by combining two LSF families using AND-construction or OR-construction _(pg. 101 of book)_.

The next section discusses different distance metrics, and thus gives an idea of the functions in an LSF family of interest.

### 2. String distance metrics

String metrics map a pair of strings (S, T) to a real number R. A survey of string matching techniques is available in the paper _[A Comparison of String Distance Metrics for Name-Matching Tasks](http://www.cs.cmu.edu/~wcohen/postscript/ijcai-ws-2003.pdf)_. The paper also describes how to combine two different string distance metrics.

#### String metrics for transliterated Indic text
The paper identifies the _Jaro-Winkler metric_ as better-performing. However, since Kosha involves matching of transliterated Indic text, a combination of a phonetic scheme such as the _Soundex metric_ and the _Jaro-Winkler metric_ seems more promising. Also, since different words may have similar endings (e.g. manohari, malahari, bilahari) or similar beginnings (e.g. hamsanārāyani, hamsanaadam), a token-based distance metric as described in the paper could be used, after tokens have been identified based on token frequencies in each dataset.

#### Distance metrics based on context
Distance and similarity metrics are more general than string metrics. They can also look at contextual information in cases where using only a string metric gives rise to uncertainity. **E.g.**: if a raga name-string S from Sangeethapriya and a raga name-string K from Karnatik are only 75% likely to be equivalent based on the above scheme, we can check for the kritis associated with S and K in the respective databases and see how well they match. Thus, distance metrics map a pair of strings (S,T) and their shared context C to a real number R. 

#### Other notions of distance
The definition of a distance metric is not limited to the inherent properties of the string/entity. For example, the _normalized Google distance (NGD)_ between two words S and T is defined as a function of the number of times T occurs in the Google search results of the word S ([source](https://arxiv.org/pdf/0901.4180.pdf)).