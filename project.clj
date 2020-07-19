(defproject kosha "0.1.0-SNAPSHOT"
  :description "Open Carnatic Music Database"
  :url "http://r4g4.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[bidi "1.19.0"]
                 [cheshire "5.4.0"]
                 [clj-http "2.0.0"]
                 [com.taoensso/timbre "2.7.1"]
                 [hiccup "1.0.5"]
                 [hikari-cp "1.5.0"]
                 [levand/immuconf "0.1.0"]
                 [medley "1.3.0"]
                 [pg-hstore "3.0.3"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [org.clojure/tools.cli "0.3.3"]
                 [org.jsoup/jsoup       "1.8.3"]
                 [org.postgresql/postgresql "42.2.14.jre7"]
                 [ring-middleware-format "0.3.2" :exclusions [ring]]
                 [ring/ring-core "1.5.0"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [ring/ring-json "0.4.0"]]
  :plugins []
  :jvm-opts ["-XX:-OmitStackTraceInFastThrow"]
  :main kosha.main)
