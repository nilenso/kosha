(ns kosha.app.middleware.cors
  (:require [kosha.app.util :as util]))

(defn wrap-cors-policy
  [handler]
  (fn [request]
    (let [response (handler request)]
      (-> response
          (assoc-in [:headers "Access-Control-Allow-Origin"] (util/get-config :cors :allowed-origins))
          (assoc-in [:headers "Access-Control-Allow-Methods"] (util/get-config :cors :allowed-methods))))))
