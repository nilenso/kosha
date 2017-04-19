(ns kosha.db.kriti
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [kosha.db.util :as db-util]
            [pg-hstore.core :as hs]))

(defn to-kebab-case
  "Takes a keyword with spaces and converts it to a kebab-cased keyword."
  [key-with-spaces]
  (let [key-string         (name key-with-spaces)
        kebab-cased-string (str/replace key-string #" " "-")]
    (keyword kebab-cased-string)))

(defn rename-stanza-keys
  "Renames keys of stanzas to replace spaces in them, if the lyrics has named stanzas."
  [content]
  (let [lyrics-map   (hs/from-hstore content)
        stanza-keys  (keys lyrics-map)
        new-names    (into {} (map (juxt identity to-kebab-case) stanza-keys))]
    (set/rename-keys (hs/from-hstore content) new-names)))

(defn remove-keys
  "Removes all keys in the given collection from the given map."
  [map keys-coll]
  (apply dissoc map keys-coll))

(defn parse-charanams
  "Parses charanams into a vector under :charanams key if the lyrics has named stanzas"
  [content]
  (let [max-charanams          (count (keys content))
        charanam-key-names     (map #(str "caraNam-" %) (range 1 max-charanams))
        possible-charanam-keys (concat [:caraNam] (map keyword charanam-key-names))
        charanams              (into [] (vals (select-keys content possible-charanam-keys)))]
    (-> content
        (assoc :charanams charanams)
        (remove-keys possible-charanam-keys))))

(defn parse-lyrics
  "Parses lyrics of a kriti into a clojure map if it has named stanzas."
  [lyrics]
  (as-> lyrics lyr
    (when lyr
      (-> lyr
          (hs/from-hstore)
          (update :has-named-stanzas (partial = "true"))))
    (if (:has-named-stanzas lyr)
      (-> lyr
          (update :content rename-stanza-keys)
          (update :content parse-charanams))
      lyr)))

(defn kriti
  "Retrieve details of a kriti given it's kriti_id."
  [kriti-id]
  (let [q ["SELECT k.kriti_id, k.name, k.composer, k.taala, k.url, k.lyrics, k.meaning, k.ragam_id, k.data_source, r.name AS ragam_name
            FROM kritis k
            JOIN ragams r
            ON k.kriti_id = ? AND r.ragam_id = k.ragam_id; "
           kriti-id]]
    (-> (first (db-util/run-query q))
        (update :lyrics parse-lyrics))))

(defn renditions-of-kriti
  "Retrieve all renditions of a kriti."
  [kriti-id]
  (let [q ["SELECT rendition_id, concert_id, concert_url, track_number, url, main_artist, kriti_name, kriti_id
            FROM renditions
            WHERE kriti_id = ?; "
           kriti-id]]
    (vec (db-util/run-query q))))
