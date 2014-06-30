(ns camel-snake-kebab.core
  (:require [clojure.string]
            [camel-snake-kebab.case-convert]
            #+clj [camel-snake-kebab.macros :refer [defconversion]])
  #+clj  (:import (clojure.lang Keyword Symbol))
  #+cljs (:require-macros [camel-snake-kebab.macros :refer [defconversion]]))

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

(defconversion "CamelCase"        clojure.string/capitalize clojure.string/capitalize "")
(defconversion "Camel_Snake_Case" clojure.string/capitalize clojure.string/capitalize "_")
(defconversion "camelCase"        clojure.string/lower-case clojure.string/capitalize "" )
(defconversion "Snake_case"       clojure.string/capitalize clojure.string/lower-case "_")
(defconversion "SNAKE_CASE"       clojure.string/upper-case clojure.string/upper-case "_")
(defconversion "snake_case"       clojure.string/lower-case clojure.string/lower-case "_")
(defconversion "kebab-case"       clojure.string/lower-case clojure.string/lower-case "-")
(defconversion "HTTP-Header-Case" camel-snake-kebab.case-convert/capitalize-http-header camel-snake-kebab.case-convert/capitalize-http-header "-")
