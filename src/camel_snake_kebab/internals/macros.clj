(ns camel-snake-kebab.internals.macros
  (:require [clojure.string :refer [join]]
            [camel-snake-kebab.internals.misc :refer [convert-case]]))

(defn ^:private type-preserving-function [case-label first-fn rest-fn sep]
  `(let [convert-case# (partial convert-case ~first-fn ~rest-fn ~sep)]
     (defn ~(->> case-label (format "->%s") symbol) [s#]
       (camel-snake-kebab.core/alter-name s# convert-case#))))

(defn ^:private type-converting-functions [case-label first-fn rest-fn sep]
  (letfn [(make-name [type-label]
            (->> [case-label type-label]
                 (join \space)
                 (convert-case (resolve first-fn) (resolve rest-fn) sep)
                 (format "->%s")
                 (symbol)))]
    (for [[type-label type-converter] {"string" `identity "symbol" `symbol "keyword" `keyword}]
      `(defn ~(make-name type-label) [s#]
         (->> s#
              name
              (convert-case ~first-fn ~rest-fn ~sep)
              ~type-converter)))))

(defmacro defconversion [case-label first-fn rest-fn sep]
  `(do  ~(type-preserving-function  case-label first-fn rest-fn sep)
       ~@(type-converting-functions case-label first-fn rest-fn sep)))
