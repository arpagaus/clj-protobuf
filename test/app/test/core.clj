(ns app.test.core
  (:use [app.core])
  (:use [clojure.test]))

(deftest addition
  (is (= 4 (+ 2 2)))
  (is (= 7 (+ 3 4)))
  (is (= 5 (protobuf-load 5))))

(def simple-schema
     {:type :message
      :name "Person"
      :content [{:type :int32 :name "age" :tag 1}]})
(def simple-message {:age 150})

(deftest protobuf-dump-test
  (is (= (seq (byte-array [(unchecked-byte 0x08)
                      (unchecked-byte 0x96)
                      (unchecked-byte 0x01)]))
         (protobuf-dump simple-schema simple-message))))

(deftest protobuf-compute-size-test
  (is (= 3 (protobuf-compute-size simple-schema simple-message)))
  (is (= 4 (protobuf-compute-size simple-schema {:age 32767}))))
