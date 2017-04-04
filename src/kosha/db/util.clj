(ns kosha.db.util)

(defn ->hyphens [^String x]
  (keyword (.replace x \_ \-)))
