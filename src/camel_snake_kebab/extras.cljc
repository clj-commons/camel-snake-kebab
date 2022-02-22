(ns camel-snake-kebab.extras
  (:require [clojure.walk :refer [postwalk]]))

(defn transform-keys
  "Recursively transforms all map keys in coll with t."
  [t coll]
  (letfn [(transform [[k v]] [(t k) v])]
    (postwalk (fn [x] (if (map? x) (with-meta (into {} (map transform x)) (meta x)) x)) coll)))
