(ns kosha.app
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.params :as params]
            [ring.middleware.keyword-params :as kw-params]
            [cider.nrepl :as cider]
            [clojure.tools.nrepl.server :as nrepl]
            [bidi.ring :as br]
            [kosha.app.search :as search]))

(def routes ["/" [["" (br/->ResourcesMaybe {:prefix "public/"})]
                  ["search" search/handler]
                  ["search/" {[:type "/"] {[:query] search/handler}}]]])

(def main-handler
  (-> (br/make-handler routes)
      kw-params/wrap-keyword-params
      params/wrap-params))

(defn start! [port nrepl-port]
  (nrepl/start-server :port nrepl-port :handler cider/cider-nrepl-handler)
  (jetty/run-jetty main-handler {:port port}))
