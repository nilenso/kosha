(ns kosha.scrapers.karnatik-test-data
  (:require [clojure.edn :as edn]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [kosha.scrapers.util :as util])
  (:import org.jsoup.Jsoup
           [org.jsoup.nodes Document Element]
           org.jsoup.select.Elements))

(def output-filename "output/test-data.edn")
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
  [ragam-anchor-tags]
  (->> ragam-anchor-tags
       (map (juxt ragam-name has-children?))
       (vec)))

(defn grouped-ragams
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
  [name-sets]
  (mapcat seq
          (for [name-set name-sets]
            (let [name (first (first (filter second name-set)))
                  synonym-names (apply hash-set (map first name-set))]
              [name synonym-names]))))

(defn get-all-ragams
  []
  (into {}
        (for [char karnatik-chars]
          (let [syns (synonyms (ragam-name-sets char))]
            (hash-map (keyword char) (apply hash-map syns))))))

(defn write-to-file
  []
  (spit output-filename (-> (get-all-ragams) pp/pprint with-out-str)))
