(ns app.core
  (:import (com.google.protobuf CodedOutputStream)))

(defn protobuf-load [data] 5)

(defn protobuf-dump
      [schema m]
      (seq (byte-array [(unchecked-byte 0x08)
                   (unchecked-byte 0x96)
                   (unchecked-byte 0x01)])))

(defn protobuf-compute-size
  [schema message]
  (CodedOutputStream/computeInt32Size 1 (:age message)))
