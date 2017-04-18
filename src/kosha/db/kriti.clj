(ns kosha.db.kriti
  (:require [kosha.db.util :as db-util]))

(defn kriti
  "Retrieve details of a kriti given it's kriti_id."
  [kriti-id]
  (let [q ["SELECT k.kriti_id, k.name, k.composer, k.taala, k.url, k.lyrics, k.meaning, k.ragam_id, k.data_source, r.name AS ragam_name
            FROM kritis k
            JOIN ragams r
            ON k.kriti_id = ? AND r.ragam_id = k.ragam_id; "
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
