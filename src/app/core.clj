(ns app.core
  (:import (com.google.protobuf CodedOutputStream)))

(defn protobuf-load [data] 5)

(defn protobuf-dump
  [schema m]
  (seq (byte-array [(unchecked-byte 0x08)
                    (unchecked-byte 0x96)
                    (unchecked-byte 0x01)])))

(defn protobuf-compute-attribute-size
  [type tag value]
  (case type
    :double (CodedOutputStream/computeDoubleSize tag value)
    :float (CodedOutputStream/computeFloatSize tag value)
    :int32 (CodedOutputStream/computeInt32Size tag value)
    :int64 (CodedOutputStream/computeInt64Size tag value)
    :uint32 (CodedOutputStream/computeUInt32Size tag value)
    :uint64 (CodedOutputStream/computeUInt64Size tag value)
    :sint32 (CodedOutputStream/computeSInt32Size tag value)
    :sint64 (CodedOutputStream/computeSInt64Size tag value)
    :string (CodedOutputStream/computeStringSize tag value)
    ))

(defn protobuf-compute-size
  [schema message]
  (let [message-name (:message (meta message))
        message-schema (some #(when (= message-name (:name %)) (:content %)) schema)]
    (reduce +
            (map (fn [x]
                   (let [attribute-name (name x)
                         attribute-schema (some #(when (= attribute-name (:name %)) %) message-schema)]
                     (protobuf-compute-attribute-size (:type attribute-schema) (:tag attribute-schema) ((keyword attribute-name) message))))
                 (keys message)))))
