(ns camel-snake-kebab
  (:require (clojure (string :refer [split join capitalize
                                     lower-case upper-case])))
  (:import (clojure.lang Keyword Symbol)))

(def ^:private upper-case-http-headers
  "Headers that are matched against for converting to HTTP-Header_Case."
  #{"CPU" "DNT" "IP" "SSL" "TE" "UA" "WAP" "XSS"})

(defn- capitalize-http-header
  "Uppercase x if it is an HTTP header, capitalize it otherwise."
  [x]
  (or (upper-case-http-headers (upper-case x))
      (capitalize x)))

(def ^:private word-separator-pattern
  "A pattern that matches all known word separators."
  (->> [" " "_" "-"
        "(?<=[A-Z])(?=[A-Z][a-z])"
        "(?<=[^A-Z_-])(?=[A-Z])"
        "(?<=[A-Za-z])(?=[^A-Za-z])"]
       (join "|")
       re-pattern))

(defn- convert-case [first-fn rest-fn separator s]
  "Converts the case of a string s according to the rule for the first
  word, remaining words, and the separator."
  (-> s
      (split word-separator-pattern)
      ((fn [[word & more]]
         (join separator (cons (first-fn word) (map rest-fn more)))))))

(def ^:private case-conversion-rules
  "The formatting rules for each case."
  {"CamelCase" [capitalize capitalize ""]
   "Camel_Snake_Case" [capitalize capitalize "_"]
   "Camel-Kebab-Case" [capitalize capitalize "-"]
   "camelCase" [lower-case capitalize ""]
   "camel_Snake_Case" [lower-case capitalize "_"]
   "camel-Kebab-Case" [lower-case capitalize "-"]
   "Snake_case" [capitalize lower-case "_"]
   "Kebab-case" [capitalize lower-case "-"]
   "SNAKE_CASE" [upper-case upper-case "_"]
   "KEBAB-CASE" [upper-case upper-case "-"]
   "snake_case" [lower-case lower-case "_"]
   "kebab-case" [lower-case lower-case "-"]
   "HTTP-Header-Case" [capitalize-http-header capitalize-http-header "-"]})

(defprotocol AlterName
  (alter-name [this f] "Alters the name of this with f."))

(extend-protocol AlterName
  String (alter-name [this f] (-> this f))
  Keyword (alter-name [this f] (-> this name f keyword))
  Symbol (alter-name [this f] (-> this name f symbol)))

(doseq [[case-label [first-fn rest-fn separator]] case-conversion-rules]
  (let [case-converter (partial convert-case first-fn rest-fn separator)
        symbol-creator (fn [type-label]
                         (->> [case-label type-label]
                              (join \space)
                              case-converter
                              (format "->%s")
                              symbol))]
    ;; Create the type-preserving functions.
    (intern *ns*
            (->> case-label (format "->%s") symbol)
            #(alter-name % case-converter))
    ;; Create the string-returning functions.
    (intern *ns* (symbol-creator "string") (comp case-converter name))
    (doseq [[type-label type-converter] {"symbol" symbol "keyword" keyword}]
      ;; Create the symbol- and keyword-returning.
      (intern *ns*
              (symbol-creator type-label)
              (comp type-converter case-converter name)))))
