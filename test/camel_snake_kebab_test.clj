(ns camel-snake-kebab-test
  (:use clojure.test
        camel-snake-kebab)
  (:require (clojure (string :refer [split]))))

(deftest word-separator-pattern-test
  (are [x y] (= x (split y @#'camel-snake-kebab/word-separator-pattern))
    ["foo" "bar"] "foo bar"
    ["foo" "bar"] "foo\n\tbar"
    ["foo" "bar"] "foo-bar"
    ["foo" "Bar"] "fooBar"
    ["Foo" "Bar"] "FooBar"
    ["foo" "bar"] "foo_bar"
    ["FOO" "BAR"] "FOO_BAR"))

(def zip (partial map vector))

(deftest format-case-test
  (testing "examples"
    (are [x y] (= x y)
      'FluxCapacitor  (->CamelCase 'flux-capacitor)
      "I_AM_CONSTANT" (->SNAKE_CASE "I am constant")
      :object-id      (->kebab-case :object_id)
      "X-SSL-Cipher"  (->HTTP-Header-Case "x-ssl-cipher")

      :object-id      (->kebab-case-keyword "object_id")))

  (testing "all the type preserving functions"
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
          (is (= (format output) (function (format input))))))))
  
  (testing "some of the type converting functions"
    (are [x y] (= x y)
      :FooBar   (->CamelCaseKeyword  'foo-bar)
      "FOO_BAR" (->SNAKE_CASE_STRING :foo-bar)
      'foo-bar  (->kebab-case-symbol "foo bar"))))

(deftest http-header-case-test
  (are [x y] (= x (->HTTP-Header-Case y))
    "User-Agent"       "user-agent"
    "DNT"              "dnt"
    "Remote-IP"        "remote-ip"
    "TE"               "te"
    "UA-CPU"           "ua-cpu"
    "X-SSL-Cipher"     "x-ssl-cipher"
    "X-WAP-Profile"    "x-wap-profile"
    "X-XSS-Protection" "x-xss-protection"))
