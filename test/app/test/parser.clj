(ns app.test.parser
  (:use [app.parser])
  (:use [clojure.test]))

(deftest parsdef
  (is (= as-and-bs ("tree")))
  )
