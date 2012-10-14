# Camel_SNAKE-kebab

A Clojure library to convert between different word case conventions.

## Setup

1. Download the source code and execute `lein install`.

2. Add the following to your `project.clj`:

  ```clojure
  :dependencies [... [camel-snake-kebab "0.1.0-SNAPSHOT"]]
  ```

3. Add the following to your namespace declaration:

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

## Examples

```clojure
(->CamelCase 'flux-capacitor)
; => 'FluxCapacitor

(->SNAKE_CASE "I am constant")
; => "I_AM_CONSTANT"

(->kebab-case :object_id)
; => :object-id
```

## Further Reading

* [ToCamelCaseorUnderscore](http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.158.9499)

## License

Copyright (C) 2012 [Christoffer Sawicki](mailto:christoffer.sawicki@gmail.com)

Distributed under the Eclipse Public License, the same as Clojure.
