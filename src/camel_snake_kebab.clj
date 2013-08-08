(ns camel-snake-kebab
  (:use [clojure.string :only (split join capitalize lower-case upper-case)])
  (:import (clojure.lang Keyword Symbol)))

(defprotocol Stringish
  (transform [this f]))

(extend-protocol Stringish
  String  (transform [this f] (-> this f))
  Keyword (transform [this f] (-> this name f keyword))
  Symbol  (transform [this f] (-> this name f symbol)))

(def ^:private word-separators
  [" ", "_", "-"
 ; http://stackoverflow.com/a/2560017/339785 :
   "(?<=[A-Z])(?=[A-Z][a-z])"
   "(?<=[^A-Z_])(?=[A-Z])"
   "(?<=[A-Za-z])(?=[^A-Za-z])"])

(def ^:private word-separator-pattern
  (->> word-separators (join "|") re-pattern))

(def ^:private parse
  #(split % word-separator-pattern))

(defn format-case [first-fn rest-fn separator stringish]
  (letfn [(unparse [[first & rest]] (join separator (cons (first-fn first) (map rest-fn rest))))]
    (transform stringish (comp unparse parse))))

(def ->CamelCase  (partial format-case capitalize capitalize ""))
(def ->camelCase  (partial format-case lower-case capitalize ""))
(def ->SNAKE_CASE (partial format-case upper-case upper-case "_"))
(def ->Snake_case (partial format-case capitalize lower-case "_"))
(def ->snake_case (partial format-case lower-case lower-case "_"))
(def ->kebab-case (partial format-case lower-case lower-case "-"))

(def ->Camel_Snake_Case (partial format-case capitalize capitalize "_"))

(def upper-case-http-headers #{"CSP" "ATT" "WAP" "IP" "HTTP" "CPU" "DNT" "SSL" "UA" "TE" "WWW" "XSS" "MD5"})

(defn- capitalize-http-header [x]
  (or (upper-case-http-headers (upper-case x))
      (capitalize x)))

(def ->HTTP-Header-Case (partial format-case capitalize-http-header capitalize-http-header "-"))
