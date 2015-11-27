(ns camel-snake-kebab.internals.misc
  (:require [camel-snake-kebab.internals.string-separator :refer [split generic-separator]]
            [clojure.string :refer [join upper-case capitalize]]))

(defn convert-case [first-fn rest-fn sep s & {:keys [separator]
                                              :or   {separator generic-separator}}]
  (let [[first & rest] (split separator s)]
    (join sep (cons (first-fn first) (map rest-fn rest)))))

(def upper-case-http-headers
  #{"CSP" "ATT" "WAP" "IP" "HTTP" "CPU" "DNT" "SSL" "UA" "TE" "WWW" "XSS" "MD5"})

(defn capitalize-http-header [s]
  (or (upper-case-http-headers (upper-case s))
      (capitalize s)))
