(ns camel-snake-kebab.core
  (:require [clojure.string]
            [camel-snake-kebab.internals.misc :as misc]
            #?(:clj [camel-snake-kebab.internals.macros :refer [defconversion]]
               :cljs [camel-snake-kebab.internals.alter-name])) ;; Needed for expansion of defconversion
  #?(:cljs (:require-macros [camel-snake-kebab.internals.macros :refer [defconversion]])))

(defn convert-case
  "Converts the case of a string according to the rule for the first
  word, remaining words, and the separator."
  [first-fn rest-fn sep s & rest]
  (apply misc/convert-case first-fn rest-fn sep s rest))

;; These are fully qualified to workaround some issue with ClojureScript:

(defconversion "PascalCase"           clojure.string/capitalize clojure.string/capitalize "")
(defconversion "Camel_Snake_Case"     clojure.string/capitalize clojure.string/capitalize "_")
(defconversion "camelCase"            clojure.string/lower-case clojure.string/capitalize "" )
(defconversion "SCREAMING_SNAKE_CASE" clojure.string/upper-case clojure.string/upper-case "_")
(defconversion "snake_case"           clojure.string/lower-case clojure.string/lower-case "_")
(defconversion "kebab-case"           clojure.string/lower-case clojure.string/lower-case "-")
(defconversion "HTTP-Header-Case"     camel-snake-kebab.internals.misc/capitalize-http-header camel-snake-kebab.internals.misc/capitalize-http-header "-")
