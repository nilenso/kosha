(ns kosha.app.kriti
  (:require [kosha.db.kriti :as db]
            [kosha.app.util :as util]
            [pg-hstore.core :as hs]))

(defn parse-named-stanzas
  [{:keys [has-named-stanzas content] :as lyrics}]
  (if (= "true" has-named-stanzas)
    (assoc lyrics :content (hs/from-hstore content false))
    lyrics))

(defn parse-lyrics
  [lyrics]
  (when lyrics
    (-> lyrics
        hs/from-hstore
        parse-named-stanzas)))

(defn handler
  [{:keys [params] :as request}]
  (let [kriti-id   (Integer/parseInt (:kriti-id params))
        kriti      (db/kriti kriti-id)
        renditions (db/renditions-of-kriti kriti-id)]
    (util/json-response {:kriti (update kriti :lyrics parse-lyrics)
                         :renditions renditions})))
