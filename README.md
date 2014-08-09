# Camel_SNAKE-kebab

A Clojure library for word case conversions.

## News

### 2014-07-28: 0.2.0

* Supports ClojureScript!
* Requires Clojure 1.5.1 or later.
* Has new regex-free internals.
* Handles non-ASCII chars better.
* **Breaking change:** The namespace `camel-snake-kebab` has been renamed to `camel-snake-kebab.core`.

## Examples

```clojure
(use 'camel-snake-kebab.core)

(->CamelCase 'flux-capacitor)
; => 'FluxCapacitor

(->SNAKE_CASE "I am constant")
; => "I_AM_CONSTANT"

(->kebab-case :object_id)
; => :object-id

(->HTTP-Header-Case "x-ssl-cipher")
; => "X-SSL-Cipher"
```

There are also functions that convert the value type for you:

```clojure
(->kebab-case-keyword "object_id")
; => :object-id
```

## Installation

1. Add the following to your `project.clj` `:dependencies`:

  ```clojure
  [camel-snake-kebab "0.2.2-SNAPSHOT"]
  ```

2. Add the following to your namespace declaration:

  ```clojure
  :require [camel-snake-kebab.core :refer :all]
  ```

## Available Conversion Functions

* `->CamelCase`
* `->camelCase`
* `->SNAKE_CASE`
* `->Snake_case`
* `->snake_case`
* `->kebab-case`
* `->Camel_Snake_Case`
* `->HTTP-Header-Case`

You should be able to figure out all what all of them do.

Yeah, and then there are the type-converting functions:

* `->CamelCase{Keyword, String, Symbol}`
* `->camelCase{Keyword, String, Symbol}`
* `->SNAKE_CASE_{KEYWORD, STRING, SYMBOL}`
* `->Snake_case_{keyword, string, symbol}`
* `->snake_case_{keyword, string, symbol}`
* `->kebab-case-{keyword, string, symbol}`
* `->Camel_Snake_Case_{Keyword, String, Symbol}`
* `->HTTP-Header-Case-{Keyword, String, Symbol}`

## Notes

Namespaced keywords and symbols will be rejected with an exception.

## Serving Suggestions

```clojure
(defn map-keys [f m]
  (letfn [(mapper [[k v]] [(f k) (if (map? v) (map-keys f v) v)])]
    (into {} (map mapper m))))
```

### With JSON

```clojure
(require '[clojure.data.json :as json])

(json/read-str "{\"firstName\":\"John\",\"lastName\":\"Smith\"}" :key-fn ->kebab-case-keyword)
; => {:first-name "John", :last-name "Smith"}

; And back:

(json/write-str {:first-name "John", :last-name "Smith"} :key-fn ->camelCaseString)
; => "{\"firstName\":\"John\",\"lastName\":\"Smith\"}"
```

### With Plain Maps

```clojure
(map-keys ->kebab-case-keyword {"firstName" "John", "lastName" "Smith"})
; => {:first-name "John", :last-name "Smith"}

; And back:

(map-keys ->camelCaseString {:first-name "John", :last-name "Smith"})
; => {"firstName" "John", "lastName" "Smith"}
```

### With JavaBeans

```clojure
(->> (java.util.Date.)
     (bean)
     (map-keys ->kebab-case)
     :timezone-offset)
; => -120
```

### With Memoization

If you're going to do case conversion in a hot spot, use [core.memoize](https://github.com/clojure/core.memoize) to avoid doing the same conversions over and over again.

```clojure
(use 'clojure.core.memoize)
(use 'criterium.core)

(def memoized->kebab-case
  (memo-fifo ->kebab-case 512))

(quick-bench (->kebab-case "firstName"))
; ... Execution time mean : 6,384971 µs ...

(quick-bench (memoized->kebab-case "firstName"))
; ... Execution time mean : 700,146806 ns ...
```

## Further Reading

* [ToCamelCaseorUnderscore](http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.158.9499)
* [Wikipedia about usage of letter case in computer programming](http://en.wikipedia.org/wiki/Letter_case#Computers)

## Alternatives

* [org.tobereplaced/lettercase](https://github.com/ToBeReplaced/lettercase)
* [com.google.common.base.CaseFormat](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/CaseFormat.html)

## License

Copyright (C) 2012-2014 Christoffer Sawicki, ToBeReplaced & Brendan Bates

Distributed under the Eclipse Public License, the same as Clojure.
