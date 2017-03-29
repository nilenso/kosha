(ns kosha.app.util
  (:require [cheshire.core :as json]))

(defn json-response [response]
  {:status 200
   :body (json/encode response)
   :content-type "application/json"})
