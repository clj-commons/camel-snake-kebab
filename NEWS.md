# News

## 2022-05-23: 0.4.3

* `transform-keys` now keeps metadata.

## 2020-10-18: 0.4.2

* Supports self-hosted ClojureScript.
* Splits strings consistently between CLJ and CLJS.
* Doesn't throw NPE when input only consists of separator(s).
* Doesn't throw AssertionError on `nil` input.

## 2019-11-24: 0.4.1

* Declares macro-generated symbols so that tools like Cursive that use static analysis can find them.

## 2016-04-16: 0.4.0 - The Malmö Release

* Uses reader conditionals instead of cljx.
* Requires Clojure[Script] version >= 1.7.

## 2015-01-17: 0.3.0

* Optional new argument `:separator` allows you to control how words are separated.
* **Breaking changes:**
  * `CamelCase` has been renamed to `PascalCase`.
  * `SNAKE_CASE` has been renamed to `SCREAMING_SNAKE_CASE`.
  * `Snake_case` has been removed.
* The above changes makes the library AOT compilable on case-insensitive filesystems.
  In practice, this means you can e.g. use `lein ring uberwar` on Windows and OS X.

## 2014-07-28: 0.2.0

* Supports ClojureScript!
* Has new regex-free internals.
* Handles non-ASCII chars better.
* **Breaking change:** The namespace `camel-snake-kebab` has been renamed to `camel-snake-kebab.core`.
