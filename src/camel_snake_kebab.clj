(ns camel-snake-kebab
  (:use [clojure.string :only (split join capitalize lower-case upper-case)]))

(defn- fail [& xs]
  (throw (Exception. (apply str xs))))

(defn- transform-stringish [f x]
  ((cond
    (string?  x) f
    (keyword? x) (comp keyword f name)
    (symbol?  x) (comp symbol  f name)
    :else (fail "Unhandled case: " (class x)))
   x))

(def word-separators
  [" ", "_", "-"
 ; http://stackoverflow.com/a/2560017/339785 :
   "(?<=[A-Z])(?=[A-Z][a-z])"
   "(?<=[^A-Z_])(?=[A-Z])"
   "(?<=[A-Za-z])(?=[^A-Za-z])"])

(def word-separator-pattern
  (->> word-separators (join "|") re-pattern))

(def parse
  #(split % word-separator-pattern))

(defn format-case [first-fn rest-fn separator stringish]
  (letfn [(unparse [[first & rest]] (join separator (cons (first-fn first) (map rest-fn rest))))]
    (transform-stringish (comp unparse parse) stringish)))

(def ->CamelCase  (partial format-case capitalize capitalize ""))
(def ->camelCase  (partial format-case lower-case capitalize ""))
(def ->SNAKE_CASE (partial format-case upper-case upper-case "_"))
(def ->snake_case (partial format-case lower-case lower-case "_"))
(def ->kebab-case (partial format-case lower-case lower-case "-"))

(def ->Camel_Snake_Case (partial format-case capitalize capitalize "_"))
(def ->Camel_snake_case (partial format-case capitalize lower-case "_"))
