(ns kosha.app.search
  (:require [kosha.db.search :as db-search]
            [kosha.app.util :as util]))


(def no-of-results (util/get-config :api :search-results))

(defn get-ragams
  "Retrieves n best matches for given ragam name."
  [query n]
  (let [ragams (db-search/ragams query n)]
    (util/json-response ragams)))

(defn handler [{:keys [params]}]
  (let [type (:type params)
        query (:query params)]
    (case type
      "ragam" (get-ragams query no-of-results))))
