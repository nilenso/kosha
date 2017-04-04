(ns kosha.db.ragam
  (:require [clojure.java.jdbc :as j]
            [kosha.db.pool :as db-pool]
            [kosha.db.util :as db-util]))

(defn ragam
  "Retrieves details of a ragam given it's id."
  [ragam-id]
  (let [q ["SELECT ragam_id, name, arohanam, avarohanam, ragam_link AS wiki_page, melakartha, mela_ragam_id, data_source
            FROM ragams
            WHERE ragam_id = ?; "
           ragam-id]]
    (first (j/query db-pool/conn q :identifier db-util/->hyphens))))

(defn kritis-of-ragam
  "Retrieves all kritis of a ragam with the given id."
  [ragam-id]
  (let [q ["SELECT kriti_id, name, composer, taala, language, ragam_id, url, data_source
            FROM kritis
            WHERE ragam_id = ?; "
           ragam-id]]
    (vec (j/query db-pool/conn q :identifer db-util/->hyphens))))
