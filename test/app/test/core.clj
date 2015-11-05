(ns app.test.core
  (:use [app.core])
  (:use [clojure.test]))

(deftest addition
  (is (= 4 (+ 2 2)))
  (is (= 7 (+ 3 4)))
  (is (= 5 (protobuf-load 5))))

(deftest protobuf-dump-test
  (is (= (seq (byte-array [(unchecked-byte 0x08)
                      (unchecked-byte 0x96)
                      (unchecked-byte 0x01)]))
         (protobuf-dump {:age 150}))))

