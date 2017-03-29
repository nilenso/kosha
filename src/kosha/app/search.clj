(ns kosha.app.search
  (:require [kosha.db.search :as db-search]
            [kosha.app.util :as util]))


(defn get-ragam
  "Retrieves 10 best matches for given ragam name."
  [{:keys [params] :as request}]
  (let [ragams (db-search/ragams (:q params) 10)]
    (util/json-response ragams)))

(defn handler [{:keys [params] :as request}]
  (let [type (get-in request [:params :t])]
    (case type
      "ragam" (get-ragam request))))
