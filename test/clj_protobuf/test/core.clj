(ns clj-protobuf.test.core
  (:use [clj-protobuf.core])
  (:use [clj-protobuf.test.schemas])
  (:use [clojure.test])
  (:import (com.google.protobuf CodedOutputStream ByteString)))

(defn unchecked-byte-array
  "Creates a byte array for a given list of bytes"
  [bytes]
  (byte-array (map unchecked-byte bytes)))

(def trivial-data (unchecked-byte-array [0x08 0x96 0x01]))

(def simple-data (unchecked-byte-array [0x0a 0x0d 0x52 0x65 0x6d 0x6f 0x20 0x41 0x72 0x70 0x61 0x67 0x61 0x75 0x73 0x10 0x71 0x1a 0x17 0x61 0x72 0x70 0x61 0x67 0x61 0x75 0x73 0x2e 0x72 0x65 0x6d 0x6f 0x40 0x67 0x6d 0x61 0x69 0x6c 0x2e 0x63 0x6f 0x6d]))

(def intermediate-data (unchecked-byte-array [0x0a 0x0d 0x52 0x65 0x6d 0x6f 0x20 0x41 0x72 0x70 0x61 0x67 0x61 0x75 0x73 0x10 0x71 0x1a 0x17 0x61 0x72 0x70 0x61 0x67 0x61 0x75 0x73 0x2e 0x72 0x65 0x6d 0x6f 0x40 0x67 0x6d 0x61 0x69 0x6c 0x2e 0x63 0x6f 0x6d 0x20 0x02]))

(deftest protobuf-dump-trivial-test
  (is (= (seq trivial-data)
         (protobuf-dump schema-trivial "Person" {:age 150}))))

(deftest protobuf-dump-simple-test
  (is (= (seq simple-data)
         (protobuf-dump schema-simple "Person" {:name "Remo Arpagaus"
                                                :age 113
                                                :email "arpagaus.remo@gmail.com"}))))

(deftest protobuf-dump-intermediate-test
  (is (= (seq intermediate-data)
         (protobuf-dump schema-intermediate "Person" {:name "Remo Arpagaus"
                                                      :age 113
                                                      :email "arpagaus.remo@gmail.com"
                                                      :personType :PROSPECT}))))

(deftest protobuf-compute-size-test
  (is (= 2 (protobuf-compute-size schema-trivial "Person" {:age 127})))
  (is (= 3 (protobuf-compute-size schema-trivial "Person"  {:age 128})))
  (is (= 3 (protobuf-compute-size schema-simple "Person"  {:name "a"})))
  (is (= 5 (protobuf-compute-size schema-simple "Person"  {:name "a" :age 127})))
  (is (= 8 (protobuf-compute-size schema-simple "Person"  {:name "xyz" :age 128})))
  (is (= 10 (protobuf-compute-size schema-intermediate "Person"  {:name "xyz" :age 128 :personType :CUSTOMER})))
  ;;(is (= 10 (protobuf-compute-size schema-advanced "Person"  {:name "xyz" :age 128 :personType :CUSTOMER})))
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
  (is (= 3 (protobuf-compute-attribute-size :bytes 1 (unchecked-byte-array [0xff]))))
  (is (= 4 (protobuf-compute-attribute-size :bytes 1 (unchecked-byte-array [0xff 0xee]))))
  )

(deftest protobuf-compute-enum-size-test
  (is (= 2 (protobuf-compute-enum-size schema-enum-trivial "PersonType" 1 :CUSTOMER))))

(defn protobuf-dump-attribute-single
  "Encodes any given value for a single attribute to a byte buffer, assuming tag < 16"
  [type value]
  (let [buffer (byte-array (protobuf-compute-attribute-size type 15 value))
        output-stream (CodedOutputStream/newInstance buffer)]
    (do
      (protobuf-dump-attribute type 1 value output-stream)
      (seq buffer))))

(deftest protobuf-dump-attribute-test
  (is (= (seq (unchecked-byte-array [0x09 0x01 0x00 0x00 0x00 0x00 0x00 0x00 0x00])) (protobuf-dump-attribute-single :double (Double/MIN_VALUE))))
  (is (= (seq (unchecked-byte-array [0x0D 0x01 0x00 0x00 0x00])) (protobuf-dump-attribute-single :float (Float/MIN_VALUE))))
  (is (= (seq (unchecked-byte-array [0x08 0x00])) (protobuf-dump-attribute-single :int32 0)))
  (is (= (seq (unchecked-byte-array [0x08 0x7f])) (protobuf-dump-attribute-single :int32 127)))
  (is (= (seq (unchecked-byte-array [0x08 0x80 0x01])) (protobuf-dump-attribute-single :int32 128)))
  (is (= (seq (unchecked-byte-array [0x08 0x00])) (protobuf-dump-attribute-single :int64 0)))
  (is (= (seq (unchecked-byte-array [0x08 0x7f])) (protobuf-dump-attribute-single :int64 127)))
  (is (= (seq (unchecked-byte-array [0x08 0x80 0x01])) (protobuf-dump-attribute-single :int64 128)))
  (is (= (seq (unchecked-byte-array [0x08 0x00])) (protobuf-dump-attribute-single :uint32 0)))
  (is (= (seq (unchecked-byte-array [0x08 0x00])) (protobuf-dump-attribute-single :uint64 0)))
  (is (= (seq (unchecked-byte-array [0x08 0x00])) (protobuf-dump-attribute-single :sint32 0)))
  (is (= (seq (unchecked-byte-array [0x08 0x00])) (protobuf-dump-attribute-single :sint64 0)))
  (is (= (seq (unchecked-byte-array [0x0d 0x00 0x00 0x00 0x00])) (protobuf-dump-attribute-single :fixed32 0)))
  (is (= (seq (unchecked-byte-array [0x09 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00])) (protobuf-dump-attribute-single :fixed64 0)))
  (is (= (seq (unchecked-byte-array [0x08 0x00])) (protobuf-dump-attribute-single :bool false)))
  (is (= (seq (unchecked-byte-array [0x08 0x01])) (protobuf-dump-attribute-single :bool true)))
  (is (= (seq (unchecked-byte-array [0x0a 0x01 0x61])) (protobuf-dump-attribute-single :string "a")))
  (is (= (seq (unchecked-byte-array [0x0a 0x01 0x62])) (protobuf-dump-attribute-single :string "b")))
  (is (= (seq (unchecked-byte-array [0x0a 0x03 0x61 0x62 0x63])) (protobuf-dump-attribute-single :string "abc")))
  (is (= (seq (unchecked-byte-array [0x0a 0x01 0x00])) (protobuf-dump-attribute-single :bytes (unchecked-byte-array [0x00]))))
  (is (= (seq (unchecked-byte-array [0x0a 0x04 0xca 0xfe 0xba 0xbe])) (protobuf-dump-attribute-single :bytes (unchecked-byte-array [0xca 0xfe 0xba 0xbe]))))
  )

;; Only used in Ligth Table. As a result Leinigen runs tests twice.
;;(run-tests)
