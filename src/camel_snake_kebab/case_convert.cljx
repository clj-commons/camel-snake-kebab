(ns camel-snake-kebab.case-convert
  (:require [clojure.string :refer [join upper-case capitalize]]
            #+clj  [clojure.core.match :refer [match]]
            #+cljs [cljs.core.match])
  #+cljs (:require-macros [cljs.core.match.macros :refer [match]]))

(defn ^:private classify-char [c]
  (case c
    (\0 \1 \2 \3 \4 \5 \6 \7 \8 \9) :number
    (\- \_ \space \tab \newline \o013 \formfeed \return) :whitespace
    (\a \b \c \d \e \f \g \h \i \j \k \l \m \n \o \p \q \r \s \t \u \v \w \x \y) :lower
    (\A \B \C \D \E \F \G \H \I \J \K \L \M \N \O \P \Q \R \S \T \U \V \W \X \Y) :upper
    :other))

(defn ^:private split [ss]
  (let [cs (mapv classify-char ss)]
    (loop [result [], start 0, i 0]
      (let [i+1 (+ i 1)
            result+new
            (fn [end]
              (if (> end start)
                (conj result (.substring ^String ss start end))
                result))]
        (if (< i (count ss))
          (let [[m0 m1 m2]
                (match [(subvec cs i)]
                  [[:whitespace & _]]
                  [(result+new i) i+1 i+1]

                  [(:or [:lower :upper                        & _]
                        [:upper :upper :lower                 & _]
                        ;; (:not _) patterns would be nice below!
                        [:number (_ :guard #(not= % :number)) & _]
                        [(_ :guard #(not= % :number)) :number & _])]
                  [(result+new i+1) i+1 i+1]

                  :else
                  [result start i+1])]
            (recur m0 m1 m2)) ;; To avoid `recur` inside of `match`
          (result+new i))))))

(defn convert-case [first-fn rest-fn sep s]
  "Converts the case of a string according to the rule for the first
  word, remaining words, and the separator."
  (let [[first & rest] (split s)]
    (join sep (cons (first-fn first) (map rest-fn rest)))))

(def ^:private upper-case-http-headers
  #{"CSP" "ATT" "WAP" "IP" "HTTP" "CPU" "DNT" "SSL" "UA" "TE" "WWW" "XSS" "MD5"})

(defn capitalize-http-header [s]
  (or (upper-case-http-headers (upper-case s))
      (capitalize s)))
