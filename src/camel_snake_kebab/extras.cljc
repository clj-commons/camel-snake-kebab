(ns camel-snake-kebab.extras
  (:require [clojure.walk :refer [postwalk]]))

(defn transform-keys
  "Recursively transforms all map keys in coll with t."
  [t coll]
  (letfn [(transform [[k v]] [(t k) v])]
    (postwalk (fn [x] (if (map? x) (into {} (map transform x)) x)) coll)))

(defn transform-top-keys 
  "Transforms all first level map keys in coll with t."
  [t coll]
  (letfn [(transform [[k v]] [(t k) v])]
    (if (map? coll) (into {} (map transform coll)) coll)))
