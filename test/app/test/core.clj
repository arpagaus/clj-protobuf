(ns app.test.core
  (:use [app.core])
  (:use [clojure.test]))

(deftest addition
  (is (= 4 (+ 2 2)))
  (is (= 7 (+ 3 4)))
  (is (= 5 (protobuf-load 5))))

(deftest protobuf-load-simple
  (is (= (protobuf-load (byte-array [(byte 0x08)
                                     (byte 0x96)
                                     (byte 0x01)]))
         150)))
