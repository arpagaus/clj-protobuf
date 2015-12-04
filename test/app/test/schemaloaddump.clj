(ns app.test.schemaloaddump
  (:use [app.test.schemas])
  (:use [app.schemaloaddump])
  (:use [clojure.test]))

(deftest test-stupid (is (= 1 1)))

;;(println (protobuf-schema-dump schema-trivial))
;;(println (protobuf-schema-dump schema-advanced))

(deftest test-protobuf-schema-dump-trivial 
  (is (= (protobuf-schema-dump schema-trivial) "message Person {\n\trequired int32 age = 1;\n}\n")))
