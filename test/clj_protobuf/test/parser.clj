(ns clj-protobuf.test.parser
  (:use [clj-protobuf.parser])
  (:use [clj-protobuf.test.schemas])
  (:use [clj-protobuf.schemaloaddump])
  (:use [clojure.pprint])
  (:use [clojure.test]))

(def schema-simple-text (slurp "examples/simple.proto"))

(def schema-advanced-text (slurp "examples/advanced.proto"))

;; does not really test anything, just if the protobuf-parser basically works
(deftest test-protobuf-parser
  (is (= ((protobuf-parser schema-simple-text) 0) :proto)))

(deftest test-schemas
  (is (= (protobuf-schema-load schema-simple-text) schema-simple))
  (is (= (protobuf-schema-load schema-advanced-text) schema-advanced)))


;(println (protobuf-schema-load schema-advanced-text))
;(println schema-advanced)
