(ns kosha.app.ragam
  (:require [kosha.db.ragam :as db]
            [kosha.app.util :as util]))

(defn handler
  "Retrieves details of a ragam and its kritis, given the ragam-id."
  [{:keys [params] :as request}]
  (let [ragam-id (read-string (:ragam-id params))
        ragam    (db/ragam ragam-id)
        kritis   (db/kritis-of-ragam ragam-id)
        output   (assoc ragam :kritis kritis)]
    (util/json-response output)))
