(ns camel-snake-kebab.core
  (:require [clojure.string]
            [camel-snake-kebab.case-convert]
            #+clj [camel-snake-kebab.macros :refer [gen-conversion-fns]])
  #+clj  (:import (clojure.lang Keyword Symbol))
  #+cljs (:require-macros [camel-snake-kebab.macros :refer [gen-conversion-fns]]))

(defprotocol AlterName
  (alter-name [this f] "Alters the name of this with f."))

(extend-protocol AlterName
  #+clj
  String
  #+cljs
  string
  (alter-name [this f]
    (-> this f))

  Keyword
  (alter-name [this f]
    (if (namespace this)
      (throw (ex-info "Namespaced keywords are not supported" {:input this}))
      (-> this name f keyword)))

  Symbol
  (alter-name [this f]
    (if (namespace this)
      (throw (ex-info "Namespaced symbols are not supported" {:input this}))
      (-> this name f symbol))))

;; These are fully qualified to workaround some issue with ClojureScript:

(gen-conversion-fns {"CamelCase"        [clojure.string/capitalize clojure.string/capitalize "" ]
                     "Camel_Snake_Case" [clojure.string/capitalize clojure.string/capitalize "_"]
                     "camelCase"        [clojure.string/lower-case clojure.string/capitalize "" ]
                     "Snake_case"       [clojure.string/capitalize clojure.string/lower-case "_"]
                     "SNAKE_CASE"       [clojure.string/upper-case clojure.string/upper-case "_"]
                     "snake_case"       [clojure.string/lower-case clojure.string/lower-case "_"]
                     "kebab-case"       [clojure.string/lower-case clojure.string/lower-case "-"]
                     "HTTP-Header-Case" [camel-snake-kebab.case-convert/capitalize-http-header camel-snake-kebab.case-convert/capitalize-http-header "-"]})
