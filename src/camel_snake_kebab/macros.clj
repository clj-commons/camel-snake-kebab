(ns camel-snake-kebab.macros
  (:require
   [clojure.string :refer [join]]
   [camel-snake-kebab.case-convert :refer [convert-case]]))

(defn- type-converters
  [case-label first-fn rest-fn sep]
  (letfn [(make-name [type-label]
                     (->> [case-label type-label]
                                       (join \space)
                                       (convert-case (resolve first-fn) (resolve rest-fn) sep)
                                       (format "->%s")
                                       (symbol)))]
    (map (fn [[type-label type-converter]]
           `(defn ~(make-name type-label)
              [s#]
              (->> s#
                  name
                  (camel-snake-kebab.case-convert/convert-case ~first-fn ~rest-fn ~sep)
                  ~type-converter)))
         {"string" `identity "symbol" `symbol "keyword" `keyword})))

(defmacro gen-conversion-fns
  [case-conversion-rules]
  `(do ~@(map (fn [[case-label [first-fn rest-fn sep]]]
              `(let [convert-case# (partial camel-snake-kebab.case-convert/convert-case ~first-fn ~rest-fn ~sep)]
                 (defn ~(->> case-label (format "->%s") symbol)
                   [s#]
                   (camel-snake-kebab.core/alter-name s# convert-case#))
                 ~@(type-converters case-label first-fn rest-fn sep)))
            case-conversion-rules)))

