(defproject camel-snake-kebab "0.3.0-SNAPSHOT"
  :description "A library for word case conversions."
  :url "https://github.com/qerub/camel-snake-kebab"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :scm {:name "git"
        :url "https://github.com/qerub/camel-snake-kebab"}

  :dependencies []

  :profiles {:dev {:dependencies [[org.clojure/clojure "1.5.1"]
                                  [org.clojure/clojurescript "0.0-2227" :scope "provided"]]
                   :hooks [cljx.hooks]
                   :plugins [[com.keminglabs/cljx "0.4.0"]
                             [com.cemerick/clojurescript.test "0.3.1"]
                             [lein-cljsbuild "1.0.3"]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]
                                  [org.clojure/clojurescript "0.0-2371" :scope "provided"]]}}

  :jar-exclusions [#"\.cljx"]

  :source-paths ["src" "target/generated-src"]

  :test-paths ["test" "target/generated-test"]

  :cljx {:builds [{:source-paths ["src"]
                   :output-path "target/generated-src"
                   :rules :clj}
                  {:source-paths ["src"]
                   :output-path "target/generated-src"
                   :rules :cljs}
                  {:source-paths ["test"]
                   :output-path "target/generated-test"
                   :rules :clj}
                  {:source-paths ["test"]
                   :output-path "target/generated-test"
                   :rules :cljs}]}

  :cljsbuild {:builds [{:source-paths ["target/generated-src" "target/generated-test"]
                        :compiler {:output-to "target/testable.js" :optimizations :simple}}]
              :test-commands {"unit-tests" ["node" :node-runner "target/testable.js"]}}

  :aliases {"test-all" ["do" "test," "cljsbuild" "test"]})
