(ns camel-snake-kebab.internals.alter-name
  #?(:clj (:import (clojure.lang Keyword Symbol))))

(defprotocol AlterName
  (alter-name [this f] "Alters the name of this with f."))

(extend-protocol AlterName
  #?(:clj String
     :cljs string)
  (alter-name [this f]
    (-> this f))

  Keyword
  (alter-name [this f]
    (if (namespace this)
      (throw (ex-info "Namespaced keywords are not supported" {:input this}))
      (-> this name f keyword)))

  Symbol
  (alter-name [this f]
    (if (namespace this)
      (throw (ex-info "Namespaced symbols are not supported" {:input this}))
      (-> this name f symbol))))
