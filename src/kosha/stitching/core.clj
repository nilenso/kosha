(ns kosha.stitching.core
  (:require [kosha.db.util :as db-util]
            [kosha.scrapers.classified-data.karnatik-reader :as data]
            [kosha.stitching.scoring :as score]
            [kosha.stitching.util :as util]
            [loom.alg :as graph]))

(defn create-names-table
  "Creates a new table with (id, name) and returns a map of id-column name, name-column name, table-name."
  [table-name]
  (let [id-col "name_id"
        names-col "name"
        q [(format "CREATE TABLE IF NOT EXISTS %s(%s bigserial PRIMARY KEY,%s varchar(100));"
                   table-name id-col names-col)]]
    (when (db-util/execute q)
      {:name table-name
       :id-column  id-col
       :name-column names-col})))

(defn write-names-to-table
  "Inserts given collection of string into a table described by the given table objects"
  [names table]
  (when (db-util/insert-multiple-names names table)
    table))

(defn get-nodes-by-id
  "Gets the ids of nodes of a graph from database."
  [table]
  (let [q [(format "SELECT name_id FROM %s;" (:name table))]
        nodes (db-util/run-query q)]
    (set (map :name-id nodes))))

(defn get-edges-by-id
  "Gets the id->id edge list of the similarity graph, given a similarity function and a min threshold value."
  [table similarity-fn-name threshold]
  (let [q ["SELECT source, target FROM similarity_graph_id(?::text, ?::text, ?, ?);"
           (:name table) similarity-fn-name threshold comparator]
        edges (db-util/run-query q)]
    (set (map #(hash-set (:source %) (:target %)) edges))))

(defn get-nodes-by-string
  "Gets the nodes of a graph from database."
  [table]
  (let [q [(format "SELECT name FROM %s;" (:name table))]
        nodes (db-util/run-query q)]
    (set (map :name-id nodes))))

(defn get-edges-by-string
  "Gets the string->string edge list of the similarity graph, given a similarity function and a min threshold value."
  [table similarity-fn-name comparator threshold]
  (let [q ["SELECT source, target FROM similarity_graph_string(?::text, ?::text, ?, ?);"
           (:name table) similarity-fn-name threshold comparator]
        edges (db-util/run-query q)]
    (set (map #(hash-set (:source %) (:target %)) edges))))

(defn similar-ragams
  [edges]
  (graph/connected-components (util/graph edges)))


(defonce *all-ragams (data/read-scraped "output/test-data.edn"))
(defonce *classified-ragams (data/read-scraped "output/classified-test-data.edn"))
(defonce *expected-edges (data/edge-list *classified-ragams))

(defn score-strategy [f comparator threshold]
  (let [s (get-edges-by-string {:name "all_ragam_names"} f comparator threshold)]
    (def *s s)
    (score/compare-edge-lists *expected-edges s)))

(defn score-all-strategies []
  {:similarity-0.8 (score-strategy "similarity" ">" 0.8)
   :similarity-0.75 (score-strategy "similarity" ">" 0.75)
   :similarity-0.7 (score-strategy "similarity" ">" 0.7)
   :similarity-0.675 (score-strategy "similarity" ">" 0.675)
   :similarity-0.65 (score-strategy "similarity" ">" 0.65)
   :similarity-0.625 (score-strategy "similarity" ">" 0.625)
   :similarity-0.6 (score-strategy "similarity" ">" 0.6)
   :levenshtein-1 (score-strategy "levenshtein" "<=" 1)
   :soundex (score-strategy "difference" ">" 3)})



(comment
  (stencil/replace "similarity({first}, {second}) + levenshtein({first}, {second}) > {value}"
                   {:first first
                    :second second}))
