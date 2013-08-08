# Camel_SNAKE-kebab

A Clojure library to convert between different word case conventions.

## Examples

```clojure
(use 'camel-snake-kebab)

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
  [camel-snake-kebab "0.1.2-SNAPSHOT"]
  ```

2. Add the following to your namespace declaration:

  ```clojure
  :use camel-snake-kebab
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

Yeah, and then there are the type converting functions:

* `->CamelCase{Keyword, String, Symbol}`
* `->camelCase{Keyword, String, Symbol}`
* `->SNAKE_CASE_{KEYWORD, STRING, SYMBOL}`
* `->Snake_case_{keyword, string, symbol}`
* `->snake_case_{keyword, string, symbol}`
* `->kebab-case-{keyword, string, symbol}`
* `->Camel_Snake_Case_{Keyword, String, Symbol}`
* `->HTTP-Header-Case-{Keyword, String, Symbol}`

## Serving Suggestions

```clojure
(defn map-keys [f m]
  (letfn [(mapper [[k v]] [(f k) (if (map? v) (map-keys f v) v)])]
    (into {} (map mapper m))))
```

### With JSON Objects

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

## Further Reading

* [ToCamelCaseorUnderscore](http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.158.9499)
* [Wikipedia about usage of letter case in computer programming](http://en.wikipedia.org/wiki/Letter_case#Computers)

## License

Copyright (C) 2012 [Christoffer Sawicki](mailto:christoffer.sawicki@gmail.com)

Distributed under the Eclipse Public License, the same as Clojure.
