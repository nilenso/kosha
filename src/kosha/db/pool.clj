(ns kosha.db.pool
  (:require [clojure.java.jdbc :as jdbc]
            [hikari-cp.core :as hikari]
            [immuconf.config :as cfg]))

(defonce config (cfg/load "resources/config.edn"))

(def datasource-options (cfg/get config :database))

(defonce conn
  {:datasource (hikari/make-datasource datasource-options)})
