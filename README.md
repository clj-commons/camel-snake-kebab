# camel-snake-kebab

A Clojure library for word case conversions.

## News

### 2015-01-17: 0.3.0

* Optional new argument `:separator` allows you to control how words are separated.
* **Breaking changes:**
  * `CamelCase` has been renamed to `PascalCase`.
  * `SNAKE_CASE` has been renamed to `SCREAMING_SNAKE_CASE`.
  * `Snake_case` has been removed.
* The above changes makes the library AOT compilable on case-insensitive filesystems.
  In practice, this means you can e.g. use `lein ring uberwar` on Windows and OS X.

### 2014-07-28: 0.2.0

* Supports ClojureScript!
* Has new regex-free internals.
* Handles non-ASCII chars better.
* **Breaking change:** The namespace `camel-snake-kebab` has been renamed to `camel-snake-kebab.core`.

## Examples

```clojure
(use 'camel-snake-kebab.core)

(->camelCase 'flux-capacitor)
; => 'fluxCapacitor

(->SCREAMING_SNAKE_CASE "I am constant")
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

If the default way of separating words doesn't work in your use case, you can override it:

```clojure
(->snake_case :s3-key :separator \-)
; => :s3_key
```

The `:separator` argument can either be a regex, string or character.

## Installation

1. Add the following to your `project.clj` `:dependencies`:

  ```clojure
  [camel-snake-kebab "0.3.1-SNAPSHOT"]
  ```

2. Add the following to your namespace declaration:

  ```clojure
  :require [camel-snake-kebab.core :refer :all]
  ```

## Available Conversion Functions

* `->PascalCase`
* `->camelCase`
* `->SCREAMING_SNAKE_CASE`
* `->snake_case`
* `->kebab-case`
* `->Camel_Snake_Case`
* `->HTTP-Header-Case`

You should be able to figure out all what all of them do.

Yeah, and then there are the type-converting functions:

* `->PascalCase{Keyword, String, Symbol}`
* `->camelCase{Keyword, String, Symbol}`
* `->SCREAMING_SNAKE_CASE_{KEYWORD, STRING, SYMBOL}`
* `->snake_case_{keyword, string, symbol}`
* `->kebab-case-{keyword, string, symbol}`
* `->Camel_Snake_Case_{Keyword, String, Symbol}`
* `->HTTP-Header-Case-{Keyword, String, Symbol}`

## Notes

* Namespaced keywords and symbols will be rejected with an exception.

## Serving Suggestions

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
(require '[camel-snake-kebab.extras :refer [transform-keys]])

(transform-keys ->kebab-case-keyword {"firstName" "John", "lastName" "Smith"})
; => {:first-name "John", :last-name "Smith"}

; And back:

(transform-keys ->camelCaseString {:first-name "John", :last-name "Smith"})
; => {"firstName" "John", "lastName" "Smith"}
```

### With JavaBeans

```clojure
(require '[camel-snake-kebab.extras :refer [transform-keys]])

(->> (java.util.Date.)
     (bean)
     (transform-keys ->kebab-case)
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

Copyright (C) 2012-2015 the AUTHORS.

Distributed under the Eclipse Public License 1.0 (the same as Clojure).
