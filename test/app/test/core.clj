(ns app.test.core
  (:use [app.core])
  (:use [clojure.test])
  (:import (com.google.protobuf CodedOutputStream ByteString)))

(def multiple-data
  (byte-array [(unchecked-byte 0x0a)
               (unchecked-byte 0x0d)
               (unchecked-byte 0x52)
               (unchecked-byte 0x65)
               (unchecked-byte 0x6d)
               (unchecked-byte 0x6f)
               (unchecked-byte 0x20)
               (unchecked-byte 0x41)
               (unchecked-byte 0x72)
               (unchecked-byte 0x70)
               (unchecked-byte 0x61)
               (unchecked-byte 0x67)
               (unchecked-byte 0x61)
               (unchecked-byte 0x75)
               (unchecked-byte 0x73)
               (unchecked-byte 0x10)
               (unchecked-byte 0x71)
               (unchecked-byte 0x1a)
               (unchecked-byte 0x17)
               (unchecked-byte 0x61)
               (unchecked-byte 0x72)
               (unchecked-byte 0x70)
               (unchecked-byte 0x61)
               (unchecked-byte 0x67)
               (unchecked-byte 0x61)
               (unchecked-byte 0x75)
               (unchecked-byte 0x73)
               (unchecked-byte 0x2e)
               (unchecked-byte 0x72)
               (unchecked-byte 0x65)
               (unchecked-byte 0x6d)
               (unchecked-byte 0x6f)
               (unchecked-byte 0x40)
               (unchecked-byte 0x67)
               (unchecked-byte 0x6d)
               (unchecked-byte 0x61)
               (unchecked-byte 0x69)
               (unchecked-byte 0x6c)
               (unchecked-byte 0x2e)
               (unchecked-byte 0x63)
               (unchecked-byte 0x6f)
               (unchecked-byte 0x6d)
               ]))

(def single-data
  (byte-array [(unchecked-byte 0x08)
               (unchecked-byte 0x96)
               (unchecked-byte 0x01)]))


(def message-meta-person {:message "Person"})

;; schema and message with a single attribute
(def schema-single
  [{:type :message
    :name "Person"
    :content [{:type :int32 :name "age" :tag 1}]}])

(deftest protobuf-dump-simple-test
  (is (= (seq single-data)
         (protobuf-dump schema-single (with-meta {:age 150} message-meta-person)))))


;; schema and message with multiple attributes
(def schema-multiple
  [{:type :message
    :name "Person"
    :content [{:type :string :name "name" :tag 1}
              {:type :int32 :name "age" :tag 2}
              {:type :string :name "email" :tag 3}]}])

(deftest protobuf-dump-multiple-test
  (is (= (seq multiple-data)
         (protobuf-dump schema-multiple (with-meta {:name "Remo Arpagaus"
                                                    :age 113
                                                    :email "arpagaus.remo@gmail.com"} message-meta-person)))))

(deftest protobuf-compute-size-test
  (is (= 2 (protobuf-compute-size schema-single (with-meta {:age 127} message-meta-person))))
  (is (= 3 (protobuf-compute-size schema-single (with-meta  {:age 128} message-meta-person))))
  (is (= 3 (protobuf-compute-size schema-multiple (with-meta  {:name "a"} message-meta-person))))
  (is (= 5 (protobuf-compute-size schema-multiple (with-meta  {:name "a" :age 127} message-meta-person))))
  (is (= 8 (protobuf-compute-size schema-multiple (with-meta  {:name "xyz" :age 128} message-meta-person))))
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

(defn protobuf-dump-attribute-single
  "Encodes any given value for a single attribute to a byte buffer, assuming tag 1"
  [type value]
    (let [buffer (byte-array (protobuf-compute-attribute-size type 1 value))
        output-stream (CodedOutputStream/newInstance buffer)]
      (do
        (protobuf-dump-attribute type 1 value output-stream)
        (seq buffer))))

(deftest protobuf-dump-attribute-test
  (is (= (seq single-data) (protobuf-dump-attribute-single :int32 150)))
  ;; TODO add more types
  )

(run-tests)
