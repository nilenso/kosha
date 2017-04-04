(ns kosha.app.search
  (:require [kosha.db.search :as db-search]
            [kosha.app.util :as util]))


(defn get-ragams
  "Retrieves 10 best matches for given ragam name."
  [query]
  (let [ragams (db-search/ragams query 10)]
    (util/json-response ragams)))

(defn handler [{:keys [params]}]
  (let [type (:type params)
        query (:query params)]
    (case type
      "ragam" (get-ragams query))))
