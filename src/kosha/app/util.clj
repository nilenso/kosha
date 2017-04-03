(ns kosha.app.util
  (:require [cheshire.core :as json]
            [immuconf.config :as cfg]))

(defn json-response [response]
  {:status 200
   :body (json/encode response)
   :content-type "application/json"})

(defonce config (cfg/load "resources/config.edn"))
(defn get-config [& keys]
  (apply (partial cfg/get config) keys))
