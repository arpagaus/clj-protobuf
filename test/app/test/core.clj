(ns app.test.core
  (:use [app.core])
  (:use [clojure.test]))

(deftest addition
  (is (= 4 (+ 2 2)))
  (is (= 7 (+ 3 4)))
  (is (= 5 (protobuf-load 5))))

(def message-person {:message "Person"})

;; schema and message with a single attribute
(def schema-single
  [{:type :message
    :name "Person"
    :content [{:type :int32 :name "age" :tag 1}]}])

;; schema and message with multiple attributes
(def schema-multiple
  [{:type :message
    :name "Person"
    :content [{:type :int32 :name "age" :tag 1}
              {:type :string :name "name" :tag 2}]}])

(deftest protobuf-dump-test
  (is (= (seq (byte-array [(unchecked-byte 0x08)
                           (unchecked-byte 0x96)
                           (unchecked-byte 0x01)]))
         (protobuf-dump schema-single (with-meta {:age 150} message-person)))))

(deftest protobuf-compute-size-test
  (is (= 2 (protobuf-compute-size schema-single (with-meta {:age 127} message-person))))
  (is (= 3 (protobuf-compute-size schema-single (with-meta  {:age 128} message-person))))
  (is (= 3 (protobuf-compute-size schema-multiple (with-meta  {:name "a"} message-person))))
  (is (= 5 (protobuf-compute-size schema-multiple (with-meta  {:name "a" :age 127} message-person))))
  (is (= 8 (protobuf-compute-size schema-multiple (with-meta  {:name "xyz" :age 128} message-person))))
  )


;;type = "double" | "float" | "int32" | "int64" | "uint32" | "uint64"
;;      | "sint32" | "sint64" | "fixed32" | "fixed64" | "sfixed32" | "sfixed64"
;;      | "bool" | "string" | "bytes" | messageType | enumType
;; https://developers.google.com/protocol-buffers/docs/proto#scalar
(deftest protobuf-compute-attribute-size-test
  (is (= 9 (protobuf-compute-attribute-size :double 1 1.0)))
  (is (= 5 (protobuf-compute-attribute-size :float 1 1.0)))
  (is (= 2 (protobuf-compute-attribute-size :int32 1 127)))
  (is (= 3 (protobuf-compute-attribute-size :int32 1 128)))
  (is (= 3 (protobuf-compute-attribute-size :int32 1 16383)))
  (is (= 4 (protobuf-compute-attribute-size :int32 1 16384)))
  (is (= 2 (protobuf-compute-attribute-size :int64 1 127)))
  (is (= 4 (protobuf-compute-attribute-size :int64 1 16384)))
  (is (= 2 (protobuf-compute-attribute-size :uint32 1 127)))
  (is (= 3 (protobuf-compute-attribute-size :uint32 1 128)))
  (is (= 2 (protobuf-compute-attribute-size :uint64 1 127)))
  (is (= 4 (protobuf-compute-attribute-size :uint64 1 16384)))
  (is (= 2 (protobuf-compute-attribute-size :sint32 1 63)))
  (is (= 3 (protobuf-compute-attribute-size :sint32 1 64)))
  (is (= 2 (protobuf-compute-attribute-size :sint32 1 -64)))
  (is (= 3 (protobuf-compute-attribute-size :sint32 1 -65)))
  (is (= 2 (protobuf-compute-attribute-size :sint64 1 63)))
  (is (= 3 (protobuf-compute-attribute-size :sint64 1 64)))
  (is (= 2 (protobuf-compute-attribute-size :sint64 1 -64)))
  (is (= 3 (protobuf-compute-attribute-size :sint64 1 -65)))
  (is (= 5 (protobuf-compute-attribute-size :fixed32 1 42)))
  (is (= 9 (protobuf-compute-attribute-size :fixed64 1 42)))
  (is (= 2 (protobuf-compute-attribute-size :bool 1 false)))
  (is (= 2 (protobuf-compute-attribute-size :bool 1 true)))
  (is (= 3 (protobuf-compute-attribute-size :string 1 "a")))
  (is (= 4 (protobuf-compute-attribute-size :string 1 "ab")))
  (is (= 5 (protobuf-compute-attribute-size :string 1 "äb")))
  (is (= 5 (protobuf-compute-attribute-size :string 1 "äb")))
  (is (= 3 (protobuf-compute-attribute-size :bytes 1 (byte-array [(unchecked-byte 0xff)]))))
  (is (= 4 (protobuf-compute-attribute-size :bytes 1 (byte-array [(unchecked-byte 0xff) (unchecked-byte 0xee)]))))
  )


(run-tests)
