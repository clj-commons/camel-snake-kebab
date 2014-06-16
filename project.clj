(defproject camel-snake-kebab "0.1.5"
  :description "A library for word case conversions."
  :url "https://github.com/qerub/camel-snake-kebab"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :scm {:name "git"
        :url "https://github.com/qerub/camel-snake-kebab"}

  :dependencies [[org.clojure/clojure  "1.6.0"]
                 [org.clojure/clojurescript "0.0-2227" :scope "provided"]]

  :plugins [[com.keminglabs/cljx "0.4.0"]
            [com.cemerick/clojurescript.test "0.3.1"]
            [lein-cljsbuild "1.0.3"]]

  :jar-exclusions [#"\.cljx"]

  :source-paths ["src" "target/classes"]

  :test-paths ["test" "target/generated-test"]

  :cljx {:builds [{:source-paths ["src"]
                   :output-path "target/classes"
                   :rules :clj}
                  {:source-paths ["src"]
                   :output-path "target/classes"
                   :rules :cljs}
                  {:source-paths ["test"]
                   :output-path "target/generated-test"
                   :rules :clj}
                  {:source-paths ["test"]
                   :output-path "target/generated-test"
                   :rules :cljs}]}
  :cljsbuild
  {:builds
   [{:source-paths ["target/classes" "target/generated-test"]
     :compiler {:output-to "target/testable.js"
                :source-map "target/testable.js.map"
                :optimizations :simple}}]
   :test-commands {"unit-tests" ["phantomjs" :runner
                                 "target/testable.js"]}}

  :aliases
  {"test-all" ["do" "clean," "cljx" "once," "test," "cljsbuild" "test"]})
