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

(deftest format-case-test
  (testing "examples"
    (is (= 'FluxCapacitor  (->CamelCase 'flux-capacitor)))
    (is (= "I_AM_CONSTANT" (->SNAKE_CASE "I am constant")))
    (is (= :object-id      (->kebab-case :object_id))))

  (testing "all the combinations"
    (def test-inputs
      ["fooBar"
       "FooBar"
       "foo_bar"
       "FOO_BAR"
       "foo-bar"])

    (def test-functions
      [->camelCase
       ->CamelCase
       ->snake_case
       ->SNAKE_CASE
       ->kebab-case])

    (def test-formats
      [identity
       keyword
       symbol])

    (def zip (partial map vector))

    (dorun
      (for [input test-inputs, format test-formats, [output function] (zip test-inputs test-functions)]
        (is (= (format output) (function (format input))))))))
