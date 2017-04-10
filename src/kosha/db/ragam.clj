(ns kosha.db.ragam
  (:require [kosha.db.util :as db-util]))

(defn ragam
  "Retrieves details of a ragam given it's id."
  [ragam-id]
  (let [q ["SELECT ragam_id, name, arohanam, avarohanam, ragam_link AS wiki_page, melakartha, mela_ragam_id, data_source
            FROM ragams
            WHERE ragam_id = ?; "
           ragam-id]]
    (first (db-util/run-query q))))

(defn kritis-of-ragam
  "Retrieves all kritis of a ragam with the given id."
  [ragam-id]
  (let [q ["SELECT kriti_id, name, composer, taala, language, ragam_id, url, data_source
            FROM kritis
            WHERE ragam_id = ?; "
           ragam-id]]
    (vec (db-util/run-query q))))
