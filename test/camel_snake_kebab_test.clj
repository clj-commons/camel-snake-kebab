(ns camel-snake-kebab-test
  (:use clojure.test
        camel-snake-kebab))

(deftest parse-test
  (is (= ["foo" "bar"] (parse "foo bar")))
  (is (= ["foo" "bar"] (parse "foo-bar")))
  (is (= ["foo" "Bar"] (parse "fooBar")))
  (is (= ["Foo" "Bar"] (parse "FooBar")))
  (is (= ["foo" "bar"] (parse "foo_bar")))
  (is (= ["FOO" "BAR"] (parse "FOO_BAR"))))

(def zip (partial map vector))

(deftest format-case-test
  (testing "examples"
    (is (= 'FluxCapacitor  (->CamelCase 'flux-capacitor)))
    (is (= "I_AM_CONSTANT" (->SNAKE_CASE "I am constant")))
    (is (= :object-id      (->kebab-case :object_id))))

  (testing "all the combinations"
    (let
      [inputs    ["fooBar"
                  "FooBar"
                  "foo_bar"
                  "FOO_BAR"
                  "foo-bar"]
       functions [->camelCase
                  ->CamelCase
                  ->snake_case
                  ->SNAKE_CASE
                  ->kebab-case]
       formats   [identity keyword symbol]]

      (dorun
        (for [input inputs, format formats, [output function] (zip inputs functions)]
          (is (= (format output) (function (format input)))))))))
