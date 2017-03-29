(ns kosha.app
  (:require [bidi.ring :as br]
            [cider.nrepl :as cider]
            [clojure.tools.nrepl.server :as nrepl]
            [kosha.app.middleware.logging :as logging]
            [kosha.app.search :as search]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.params :as params]
            [ring.middleware.keyword-params :as kw-params]))

(def routes ["/" [["" (br/->ResourcesMaybe {:prefix "public/"})]
                  ["search" search/handler]
                  ["search/" {[:type "/"] {[:query] search/handler}}]]])

(def main-handler
  (-> (br/make-handler routes)
      kw-params/wrap-keyword-params
      params/wrap-params
      logging/wrap-log-request-response))

(defn start! [port nrepl-port]
  (nrepl/start-server :port nrepl-port :handler cider/cider-nrepl-handler)
  (jetty/run-jetty main-handler {:port port}))
