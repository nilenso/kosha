(ns kosha.app.middleware.logging
  (:require [taoensso.timbre :as log]))

(def standard-ring-request-keys
  [:server-port :server-name :remote-addr :uri :query-string :scheme
   :headers :request-method :body :params])

(defn wrap-log-request-response
 "Logs requests to the server and responses with INFO level."
 [handler]
  (fn [request]
   (log/debug {:event    ::request
               :request  (select-keys request standard-ring-request-keys)})
   (let [response (handler request)]
     (log/debug {:event    ::response
                 :response response})
     response)))
