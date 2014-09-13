(ns camel-snake-kebab.internals.macros
  (:require [clojure.string :refer [join]]
            [camel-snake-kebab.internals.misc :refer [convert-case]]))

(defn type-preserving-function [case-label first-fn rest-fn sep]
  `(let [convert-case# (partial convert-case ~first-fn ~rest-fn ~sep)]
     (defn ~(symbol (str "->" case-label)) [s#]
       (camel-snake-kebab.core/alter-name s# convert-case#))))

(defn type-converting-functions [case-label first-fn rest-fn sep]
  (letfn [(make-name [type-label]
            (->> (str case-label " " type-label)
                 (convert-case (resolve first-fn) (resolve rest-fn) sep)
                 (str "->")
                 symbol))]
    (for [[type-label type-converter] {"string" nil
                                       "symbol" 'symbol
                                       "keyword" 'keyword}]
      (if type-converter
        `(defn ~(make-name type-label) [s#]
           (~type-converter (convert-case ~first-fn ~rest-fn ~sep (name s#))))
        `(defn ~(make-name type-label) [s#]
           (convert-case ~first-fn ~rest-fn ~sep (name s#)))))))

(defmacro defconversion [case-label first-fn rest-fn sep]
  `(do  ~(type-preserving-function  case-label first-fn rest-fn sep)
       ~@(type-converting-functions case-label first-fn rest-fn sep)))
