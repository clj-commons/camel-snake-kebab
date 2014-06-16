(ns camel-snake-kebab.case-convert
  (:require [clojure.string :refer [join split upper-case capitalize replace]]))

(def ^:private word-boundary-pattern
  (->> ["(\\b)[-_\\s](\\b)"
        "([a-z])([A-Z])"
        "([A-Z])([A-Z][a-z])"
        "([a-z][A-Z])([A-Z])"
        "(?=[A-Z])([A-Z][a-z])"
        "([A-Za-z])(\\d)"
        "(\\d)([A-Za-z])"]
       (join "|")
       re-pattern))

#+clj
(defn mark-boundary
  "Handle word boundary replacement."
  [matches]
  (->>
   (rest matches)
   (filter identity)
   (join "_")))

#+cljs
(defn mark-boundary
  "Handle word boundary replacement."
  [& matches] ;; cljs replace func uses var arguments rather than a single vector arg in the clojure equivilent
  (->>
   (rest matches)
   (drop-last 2) ;; clojurescript replacement func adds the string offset and whole string as the last 2 arguments - don't need these
   (filter identity)
   (join "_")))

(defn- mark-word-boundaries
  "Mark the word boundaries in a string with an underscore. Parsed twice to pick up overlapping boundaries (single letter/number words)"
  [s]
  (-> s
      ;; pass a second time to pick up overlapped word boundaries.
      ;; Lookarounds would be nicer, but clojurescript (js) doesn't support lookbehinds
      (replace word-boundary-pattern mark-boundary)
      (replace word-boundary-pattern mark-boundary)))

(defn- split-to-words
  "Split the string into a vector of words"
  [s]
  (->
   s
   mark-word-boundaries
   (split #"_")))

(defn convert-case [first-fn rest-fn sep s]
  "Converts the case of a string according to the rule for the first
  word, remaining words, and the separator."
  (let [[first & rest] (split-to-words s)]
    (join sep (cons (first-fn first) (map rest-fn rest)))))

(def ^:private upper-case-http-headers
  #{"CSP" "ATT" "WAP" "IP" "HTTP" "CPU" "DNT" "SSL" "UA" "TE" "WWW" "XSS" "MD5"})

(defn capitalize-http-header [s]
  (or (upper-case-http-headers (upper-case s))
      (capitalize s)))
