(ns camel-snake-kebab.internals.macros
  (:require [camel-snake-kebab.internals.alter-name :refer [alter-name]]
            [camel-snake-kebab.internals.misc :refer [convert-case]]))

(defn type-preserving-function [case-label first-fn rest-fn sep]
  `(defn ~(symbol (str "->" case-label)) [s# & rest#]
     (let [convert-case# #(apply convert-case ~first-fn ~rest-fn ~sep % rest#)]
       (alter-name s# convert-case#))))

(defn type-converting-functions [case-label first-fn rest-fn sep]
  (letfn [(make-name [type-label]
            (->> (str case-label " " type-label)
                 (convert-case (resolve first-fn) (resolve rest-fn) sep)
                 (str "->")
                 (symbol)))]
    (for [[type-label type-converter] {"string" `identity "symbol" `symbol "keyword" `keyword}]
      `(defn ~(make-name type-label) [s# & rest#]
         {:pre [(not (nil? s#))]}
         (~type-converter (apply convert-case ~first-fn ~rest-fn ~sep (name s#) rest#))))))

(defmacro defconversion [case-label first-fn rest-fn sep]
  `(do  ~(type-preserving-function  case-label first-fn rest-fn sep)
       ~@(type-converting-functions case-label first-fn rest-fn sep)))
