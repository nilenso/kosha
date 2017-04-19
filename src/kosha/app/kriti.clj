(ns kosha.app.kriti
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [kosha.db.kriti :as db]
            [kosha.app.util :as util]
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

(defn handler
  [{:keys [params] :as request}]
  (let [kriti-id   (Integer/parseInt (:kriti-id params))
        kriti      (db/kriti kriti-id)
        renditions (db/renditions-of-kriti kriti-id)]
    (util/json-response {:kriti (update kriti :lyrics parse-lyrics)
                         :renditions renditions})))
