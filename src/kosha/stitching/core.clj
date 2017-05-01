(ns kosha.stitching.core
  (:require [kosha.db.util :as db-util]))

(defn create-names-table
  "Creates a new table with (id, name) and returns a map of id-column name, name-column name, table-name"
  [table-name]
  (let [id-col-name "name_id"
        names-col-name "name"
        q [(str "CREATE TABLE IF NOT EXISTS "
                table-name "("
                id-col-name " bigserial PRIMARY KEY,"
                names-col-name " varchar(100)" ");")]]
    (when (db-util/execute q)
      {:table table-name
       :id-column  id-col-name
       :name-column names-col-name})))


(defn write-names-to-db
  "Takes a collection of strings and inserts it into a temporary table in database, and returns the new table name"
  [names]
  (let [table-name (str (gensym "temp"))
        tmp-table (create-names-table table-name)]
    (when (db-util/insert-multiple names tmp-table)
      tmp-table)))


(defn get-edge-list
  "Gets the edge list of the similarity graph, given a similarity function"
  [table similarity-fn-name threshold-min]
  (let [q ["SELECT source, target FROM similarity_graph(?::text, ?::text, ?::int);"
           (:table table)
           similarity-fn-name threshold-min]
        results (db-util/run-query q)]
    (map (juxt #(:source %) #(:target %)) results)))

(defn get-connected-components
  []
  2)
