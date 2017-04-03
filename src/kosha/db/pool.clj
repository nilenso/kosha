(ns kosha.db.pool
  (:require [clojure.java.jdbc :as jdbc]
            [hikari-cp.core :as hikari]
            [kosha.app.util :as util]))

(def datasource-options (util/get-config :database))

(defonce conn
  {:datasource (hikari/make-datasource datasource-options)})
