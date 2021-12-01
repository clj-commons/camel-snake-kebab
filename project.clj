(defproject camel-snake-kebab "0.4.3-SNAPSHOT"
  :min-lein-version "2.5.2"

  :description "A library for word case conversions."
  :url "https://clj-commons.org/camel-snake-kebab/"
  :license {:name "Eclipse Public License 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :scm {:name "git"
        :url "https://github.com/clj-commons/camel-snake-kebab"}

  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.0"]
                                  [org.clojure/clojurescript "1.10.520" :scope "provided"]]
                   :plugins [[lein-cljsbuild "1.1.3"]
                             [lein-doo "0.1.11"]]}}

  :jar-exclusions [#"\.DS_Store"]

  :source-paths ["src"]

  :test-paths ["test"]

  :cljsbuild {:builds [{:id "test"
                        :source-paths ["src" "test"]
                        :compiler {:output-to "target/testable.js"
                                   :main "camel-snake-kebab.test-runner"
                                   :target :nodejs
                                   :optimizations :simple}}]}

  :aliases {"test-all" ["do" "test," "doo" "node" "test" "once," "doo" "planck" "test" "once"]})
