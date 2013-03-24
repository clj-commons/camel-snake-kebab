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
```

## Installation

1. Add the following to your `project.clj` `:dependencies`:

  ```clojure
  [camel-snake-kebab "0.1.0"]
  ```

2. Add the following to your namespace declaration:

  ```clojure
  :use camel-snake-kebab
  ```

## Available Conversion Functions

* `->CamelCase`
* `->camelCase`
* `->SNAKE_CASE`
* `->snake_case`
* `->kebab-case`

You should be able to figure out all what all of them do.

## A Serving Suggestion: Dealing with JSON Objects

```clojure
(defn map-keys [f m]
  (letfn [(mapper [[k v]] [(f k) (if (map? v) (map-keys f v) v)])]
    (into {} (map mapper m))))

(map-keys (comp ->kebab-case keyword) {"firstName" "John", "lastName" "Smith"})
; => {:first-name "John", :last-name "Smith"}

; And back:

(map-keys (comp ->camelCase name) {:first-name "John", :last-name "Smith"})
; => {"firstName" "John", "lastName" "Smith"}
```

## Further Reading

* [ToCamelCaseorUnderscore](http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.158.9499)

## License

Copyright (C) 2012 [Christoffer Sawicki](mailto:christoffer.sawicki@gmail.com)

Distributed under the Eclipse Public License, the same as Clojure.
