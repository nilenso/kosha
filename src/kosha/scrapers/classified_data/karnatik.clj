(ns kosha.scrapers.classified-data.karnatik
  (:require [clojure.edn :as edn]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [kosha.scrapers.util :as util])
  (:import org.jsoup.Jsoup
           [org.jsoup.nodes Document Element]
           org.jsoup.select.Elements))

(def output-filename "output/classified-test-data.edn")
(def test-data-output-filename "output/test-data.edn")

(defn karnatik-url
  [letter]
  (str "http://www.karnatik.com/ragas" letter ".shtml"))

(defn char-range [start end]
  (map char (range (int start) (inc (int end)))))

(def karnatik-chars
  ["a" "b" "c" "d" "e" "g" "h" "i" "j" "k" "l" "m" "n" "o" "p" "r" "s" "t" "u" "v" "w" "y"])

(defn ragam-name
  [jsoup-anchor-tag]
  (.attr jsoup-anchor-tag "name"))

(defn has-children?
  [jsoup-anchor-tag]
  (not (empty? (.children jsoup-anchor-tag))))

(defn ragam-anchor-tags
  "Call the url to scrape, and obtain the data of interest, i.e. ragam names."
  [url]
  (-> (util/url->jsoup url)
      (.select "tbody")
      (.select "td[width=420]")
      (.select "font")
      (.select "font")
      (.select "blockquote")
      (.select "a[name]")))

(defn add-separators
  "Reducing function that adds nil value in between different ragas in vector."
  [acc elem]
  (cond (empty? acc) (conj acc elem)
        (second elem) (conj acc nil elem)
        (not (second elem)) (conj acc elem)))

(defn ragam-name-vectors
  "Convert each jsoup anchor tag in argument to ['ragamname', boolean] pair."
  [ragam-anchor-tags]
  (->> ragam-anchor-tags
       (map (juxt ragam-name has-children?))
       (vec)))

(defn grouped-ragams
  "Take a vector of ragam vectors, and group the vectors if they are synonyms for the same ragam."
  [ragam-vectors]
  (->> ragam-vectors
       (reduce add-separators [])
       (partition-by nil?)
       (remove #(nil? (first %)))))

(defn ragam-name-sets
  [letter]
  (->> (ragam-anchor-tags (karnatik-url letter))
       (ragam-name-vectors)
       (rseq)
       (grouped-ragams)))

(defn synonyms
  "Given a vector of grouped ragams as ['ragam-name', boolean] pairs, convert to kv, where v is the set of synonyms."
  [name-sets]
  (mapcat seq
          (for [name-set name-sets]
            (let [name (first (first (filter second name-set)))
                  synonym-names (apply hash-set (map first name-set))]
              [name synonym-names]))))

(defn get-all-classified-ragams
  "Return a map where keys are beginning letters, and values are maps of ragam-name->synonyms."
  []
  (into {}
        (vec (for [char karnatik-chars]
               (let [syns (synonyms (ragam-name-sets char))]
                 (apply hash-map syns))))))

(defn get-all-ragam-names
  []
  (flatten (for [letter karnatik-chars]
             (->> (karnatik-url letter)
                  ragam-anchor-tags
                  (map ragam-name)
                  (vec)))))

(defn write-to-file
  []
  (spit output-filename (-> (get-all-classified-ragams) pp/pprint with-out-str))
  (println "Wrote classified test data.")
  (spit test-data-output-filename (-> (get-all-ragam-names) pp/pprint with-out-str)))
