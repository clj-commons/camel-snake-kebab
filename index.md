# Examples

```clojure
(require '[camel-snake-kebab.core :as csk])

(csk/->camelCase 'flux-capacitor)
; => 'fluxCapacitor

(csk/->SCREAMING_SNAKE_CASE "I am constant")
; => "I_AM_CONSTANT"

(csk/->kebab-case :object_id)
; => :object-id

(csk/->HTTP-Header-Case "x-ssl-cipher")
; => "X-SSL-Cipher"
```

There are also functions that convert the value type for you:

```clojure
(csk/->kebab-case-keyword "object_id")
; => :object-id
```

If the default way of separating words doesn't work in your use case, you can override it:

```clojure
(csk/->snake_case :s3-key :separator \-)
; => :s3_key
```

The `:separator` argument can either be a regex, string or character.

# Installation

Add the following to your `project.clj` `:dependencies`:

```clojure
[camel-snake-kebab "0.4.0"]
```

Add the following to your namespace declaration:

```clojure
(:require [camel-snake-kebab.core :as csk])
```

# Available Conversion Functions

* `csk/->PascalCase`
* `csk/->camelCase`
* `csk/->SCREAMING_SNAKE_CASE`
* `csk/->snake_case`
* `csk/->kebab-case`
* `csk/->Camel_Snake_Case`
* `csk/->HTTP-Header-Case`

You should be able to figure out all what all of them do.

Yeah, and then there are the type-converting functions:

* `csk/->PascalCase{Keyword, String, Symbol}`
* `csk/->camelCase{Keyword, String, Symbol}`
* `csk/->SCREAMING_SNAKE_CASE_{KEYWORD, STRING, SYMBOL}`
* `csk/->snake_case_{keyword, string, symbol}`
* `csk/->kebab-case-{keyword, string, symbol}`
* `csk/->Camel_Snake_Case_{Keyword, String, Symbol}`
* `csk/->HTTP-Header-Case-{Keyword, String, Symbol}`

# Notes

* Namespaced keywords and symbols will be rejected with an exception.

# Serving Suggestions

## With JSON

```clojure
(require '[clojure.data.json :as json])

(json/read-str "{\"firstName\":\"John\",\"lastName\":\"Smith\"}" :key-fn csk/->kebab-case-keyword)
; => {:first-name "John", :last-name "Smith"}

; And back:

(json/write-str {:first-name "John", :last-name "Smith"} :key-fn csk/->camelCaseString)
; => "{\"firstName\":\"John\",\"lastName\":\"Smith\"}"
```

## With Plain Maps

```clojure
(require '[camel-snake-kebab.extras :as cske])

(cske/transform-keys csk/->kebab-case-keyword {"firstName" "John", "lastName" "Smith"})
; => {:first-name "John", :last-name "Smith"}

; And back:

(cske/transform-keys csk/->camelCaseString {:first-name "John", :last-name "Smith"})
; => {"firstName" "John", "lastName" "Smith"}
```

## With Memoization

If you're going to do case conversion in a hot spot, use [core.memoize](https://github.com/clojure/core.memoize) to avoid doing the same conversions over and over again.

```clojure
(require '[clojure.core.memoize :as m])
(require '[criterium.core :as cc])

(def memoized->kebab-case
  (m/fifo csk/->kebab-case {} :fifo/threshold 512))

(cc/quick-bench (csk/->kebab-case "firstName"))
; ... Execution time mean : 6,384971 µs ...

(cc/quick-bench (memoized->kebab-case "firstName"))
; ... Execution time mean : 700,146806 ns ...
```

# Further Reading

* [ToCamelCaseorUnderscore](http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.158.9499)
* [Wikipedia about usage of letter case in computer programming](http://en.wikipedia.org/wiki/Letter_case#Computers)

# Alternatives

* [org.tobereplaced/lettercase](https://github.com/ToBeReplaced/lettercase)
* [com.google.common.base.CaseFormat](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/CaseFormat.html)

# License

Copyright (C) 2012-2019 the AUTHORS.

Distributed under the Eclipse Public License 1.0 (the same as Clojure).
