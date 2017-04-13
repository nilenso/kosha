(ns kosha.app.search
  (:require [kosha.db.search :as db-search]
            [kosha.app.util :as util]))


(def no-of-results (util/get-config :api :search-results))

(defn get-results
  "Retrieves 2n best matches for given ragam or kriti name."
  [query n]
  (let [ragams (future (db-search/ragams query n))
        kritis (future (db-search/kritis query n))]
    (util/json-response (into [] (concat @ragams @kritis)))))

(defn handler [{:keys [params]}]
  (let [query (:query params)]
    (get-results query no-of-results)))
