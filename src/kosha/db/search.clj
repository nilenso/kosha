(ns kosha.db.search
  (:require [clojure.java.jdbc :as j]
            [kosha.db.pool :as db-pool]
            [kosha.db.util :as db-util]))

(defn ragams
  "Retrieves n ragams from the db in order of similarity to the query."
  [ragam n]
  (let [q  ["SELECT ragam_id AS id, name, arohanam, avarohanam, melakartha, mela_ragam_id, ragam_link, data_source, similarity_score(name, ?) AS similarity, 'ragam' as type
             FROM ragams
             ORDER BY similarity_score(name, ?)
             DESC LIMIT ?; "
            ragam ragam n]]
    (vec (j/query db-pool/conn q :identifiers db-util/->hyphens))))

(defn kritis
  "Retrieves n ragams from the db in order of similarity to the query."
  [kriti n]
  (let [q  ["SELECT kriti_id AS id, name, composer, taala, language, similarity_score(name, ?) AS similarity, 'kriti' AS type
             FROM kritis
             ORDER BY similarity_score(name, ?)
             DESC LIMIT ?; "
           kriti kriti n]]
    (vec (j/query db-pool/conn q :identifiers db-util/->hyphens))))
