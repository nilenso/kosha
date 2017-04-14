(ns kosha.app.kriti
  (:require [kosha.db.kriti :as db]
            [kosha.app.util :as util]))

(defn handler
  [{:keys [params] :as request}]
  (let [kriti-id   (Integer/parseInt (:kriti-id params))
        kriti      (db/kriti kriti-id)
        renditions (db/renditions-of-kriti kriti-id)]
    (util/json-response {:kriti kriti
                         :renditions renditions})))
