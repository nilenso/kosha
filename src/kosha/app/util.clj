(ns kosha.app.util
  (:require [cheshire.core :as json]
            [immuconf.config :as config]))

(defn json-response [response]
  {:status 200
   :body (json/encode response)
   :content-type "application/json"})

(defonce config (config/load "resources/config.edn"))
(defn get-config [& ks]
  (apply config/get config ks))
