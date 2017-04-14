(ns kosha.db.kriti
  (:require [kosha.db.util :as db-util]))

(defn kriti
  "Retrieve details of a kriti given it's kriti_id."
  [kriti-id]
  (let [q ["SELECT kriti_id, name, composer, taala, url, lyrics, meaning, ragam_id, data_source
            FROM kritis
            WHERE kriti_id = ?; "
           kriti-id]]
    (first (db-util/run-query q))))

(defn renditions-of-kriti
  "Retrieve all renditions of a kriti."
  [kriti-id]
  (let [q ["SELECT rendition_id, concert_id, concert_url, track_number, url, main_artist, kriti_name, kriti_id
            FROM renditions
            WHERE kriti_id = ?; "
           kriti-id]]
    (vec (db-util/run-query q))))
