(ns app.test.parser
  (:use [app.parser])
  (:use [clojure.test]))

;; does not really test anything just if the protobuf-gramma basically works
(deftest parsdef
  (is (= ((protobuf-gramma "message Person {required int32 age = 1;}") 0) :proto)))
