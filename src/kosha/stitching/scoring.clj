(ns kosha.stitching.scoring
  (:require [clojure.set :as s]))

(defn compare-edge-lists
  [expected-edges edges]
  (let [matches (count edges)
        expected-matches (count expected-edges)
        true-positives (count (s/intersection expected-edges edges))]
      {:false-negatives (count (s/difference expected-edges edges))
       :false-positives (count (s/difference edges expected-edges))
       :true-positives true-positives
       :matches matches
       :expected-matches expected-matches
       :tp-exp-matches-pcnt (float (/ (* true-positives 100) expected-matches))
       :tp-matches-pcnt (float (/ (* true-positives 100) matches))}))
