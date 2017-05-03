(ns kosha.stitching.core
  (:require [kosha.db.util :as db-util]
            [kosha.scrapers.classified-data.karnatik-reader :as data]
            [kosha.stitching.scoring :as score]))

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
  [table similarity-fn-name threshold-min]
  (let [q ["SELECT source, target FROM similarity_graph_id(?::text, ?::text, ?::int);"
           (:name table) similarity-fn-name threshold-min]
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
  [table similarity-fn-name threshold-min]
  (let [q ["SELECT source, target FROM similarity_graph_string(?::text, ?::text, ?::int);"
           (:name table) similarity-fn-name threshold-min]
        edges (db-util/run-query q)]
    (set (map #(hash-set (:source %) (:target %)) edges))))

(comment
  ;; Example usage for scoring a strategy. In this case, similarity_score with min 7/10.
  (def *all-ragams (data/read-scraped "output/test-data.edn"))
  (def *classified-ragams (data/read-scraped "output/classified-test-data.edn"))
  (def *names-table (write-names-to-table all-ragams (create-names-table "temp_table")))
  ;; In case you already have a table of names, then use (def *names-table {:name "temp_table" :id-column "id" :names-column "name"})
  (def *expected-edges (data/edge-list *classified-ragams))
  (def *actual-edges (get-edges-by-string *names-table "similarity_score" 7))
  (def *score (score/compare-edge-lists *expected-edges *actual-edges)))
