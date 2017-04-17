(ns kosha.db.util
  (:require [clojure.java.jdbc :as j]
            [kosha.db.pool :as db-pool]))

(defn ->hyphens [^String x]
  (keyword (.replace x \_ \-)))

(defn run-query
  [query]
  (j/query db-pool/conn query :identifiers ->hyphens))
