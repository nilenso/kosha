(ns kosha.db.search
  (:require [clojure.java.jdbc :as j]
            [kosha.db.pool :as db-pool]))

(defn ->hyphens [^String x]
  (keyword (.replace x \_ \-)))

(defn ragams
  "Retrieves n ragams from the db in order of similarity to the query."
  [ragam n]
  (let [q  ["SELECT ragam_id, name, arohanam, avarohanam, melakartha, mela_ragam_id, ragam_link, data_source
             FROM ragams
             ORDER BY similarity_score(name, ?)
             DESC LIMIT ?; "
            ragam n]]
    (vec (j/query db-pool/conn q :identifiers ->hyphens))))
