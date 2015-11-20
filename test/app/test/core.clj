(ns app.test.core
  (:use [app.core])
  (:use [clojure.test]))

(deftest addition
  (is (= 4 (+ 2 2)))
  (is (= 7 (+ 3 4)))
  (is (= 5 (protobuf-load 5))))

;; schema and message with a single attribute
(def schema-single
  {:type :message
   :name "Person"
   :content [{:type :int32 :name "age" :tag 1}]})

;; schema and message with multiple attributes
(def schema-multiple
  {:type :message
   :name "Person"
   :content [{:type :int32 :name "age" :tag 1}
             {:type :string :name "name" :tag 2}]})

(deftest protobuf-dump-test
  (is (= (seq (byte-array [(unchecked-byte 0x08)
                           (unchecked-byte 0x96)
                           (unchecked-byte 0x01)]))
         (protobuf-dump schema-single {:age 150}))))

(deftest protobuf-compute-size-test
  (is (= 2 (protobuf-compute-size schema-single {:age 127})))
  (is (= 3 (protobuf-compute-size schema-single {:age 128})))
  (is (= 3 (protobuf-compute-size schema-single {:age 16383})))
  (is (= 4 (protobuf-compute-size schema-single {:age 16384})))
  (is (= 2 (protobuf-compute-size schema-single {:name "a"}))) ;; fix
  )


(run-tests)
