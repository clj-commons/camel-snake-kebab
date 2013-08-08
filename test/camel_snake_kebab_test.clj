(ns camel-snake-kebab-test
  (:use clojure.test
        camel-snake-kebab)
  (:require (clojure (string :refer [split]))))

(defn separate-words [s] (split s @#'camel-snake-kebab/word-separator-pattern))

(deftest word-separator-pattern-test
  (is (= ["foo" "bar"] (separate-words "foo bar")))
  (is (= ["foo" "bar"] (separate-words "foo-bar")))
  (is (= ["foo" "Bar"] (separate-words "fooBar")))
  (is (= ["Foo" "Bar"] (separate-words "FooBar")))
  (is (= ["foo" "bar"] (separate-words "foo_bar")))
  (is (= ["FOO" "BAR"] (separate-words "FOO_BAR"))))

(def zip (partial map vector))

(deftest format-case-test
  (testing "examples"
    (is (= 'FluxCapacitor  (->CamelCase 'flux-capacitor)))
    (is (= "I_AM_CONSTANT" (->SNAKE_CASE "I am constant")))
    (is (= :object-id      (->kebab-case :object_id)))
    (is (= "X-SSL-Cipher"  (->HTTP-Header-Case "x-ssl-cipher"))))

  (testing "all the combinations"
    (let
      [inputs    ["FooBar"
                  "fooBar"
                  "FOO_BAR"
                  "Foo_bar"
                  "foo_bar"
                  "foo-bar"
                  "Foo_Bar"]
       functions [->CamelCase
                  ->camelCase
                  ->SNAKE_CASE
                  ->Snake_case
                  ->snake_case
                  ->kebab-case
                  ->Camel_Snake_Case]
       formats   [identity keyword symbol]]

      (dorun
        (for [input inputs, format formats, [output function] (zip inputs functions)]
          (is (= (format output) (function (format input)))))))))

(deftest http-header-case-test
  (is (= "User-Agent"       (->HTTP-Header-Case "user-agent")))
  (is (= "DNT"              (->HTTP-Header-Case "dnt")))
  (is (= "Remote-IP"        (->HTTP-Header-Case "remote-ip")))
  (is (= "TE"               (->HTTP-Header-Case "te")))
  (is (= "UA-CPU"           (->HTTP-Header-Case "ua-cpu")))
  (is (= "X-SSL-Cipher"     (->HTTP-Header-Case "x-ssl-cipher")))
  (is (= "X-WAP-Profile"    (->HTTP-Header-Case "x-wap-profile")))
  (is (= "X-XSS-Protection" (->HTTP-Header-Case "x-xss-protection"))))
