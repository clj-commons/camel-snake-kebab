(defproject camel-snake-kebab "0.3.3-SNAPSHOT"
  :description "A library for word case conversions."
  :url "https://github.com/qerub/camel-snake-kebab"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :scm {:name "git"
        :url "https://github.com/qerub/camel-snake-kebab"}

  :dependencies []

  :profiles {:dev {:dependencies [[org.clojure/clojure "1.7.0"]
                                  [org.clojure/clojurescript "1.7.170" :scope "provided"]]
                   :plugins [[lein-cljsbuild "1.1.1"]
                             [lein-doo "0.1.6-rc.1"]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]
                                  [org.clojure/clojurescript "0.0-2371" :scope "provided"]]}}

  :min-lein-version "2.5.2"

  :source-paths ["src" "test"]

  :test-paths ["test"]

  :cljsbuild {:builds [{:id "test"
                        :source-paths ["src" "test"]
                        :compiler {:output-to "target/testable.js"
                                   :main "camel-snake-kebab.runner"
                                   :target :nodejs
                                   :optimizations :simple}}]}

  :aliases {"test-all" ["do" "test," "doo" "node" "test" "once"]})
