(ns kosha.db.search
  (:require [clojure.java.jdbc :as j]
            [clojure.set :as set]
            [kosha.db.pool :as db-pool]
            [pg-hstore.core :as hs]
            [medley.core :as m]))

(defn ->hyphens [^String x]
  (keyword (.replace x \_ \-)))

(defn ragams
  "Retrieves n ragams from the db in order of similarity to the query."
  [ragam n]
  (let [q  ["SELECT *, similarity_score(name, ?) AS score
             FROM tmp_ragams
             ORDER BY score
             DESC LIMIT ?; "
            ragam n]]
    (vec (j/query db-pool/conn q :identifiers ->hyphens))))
