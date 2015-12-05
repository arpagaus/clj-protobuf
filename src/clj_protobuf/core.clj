(ns clj-protobuf.core
  (:import (com.google.protobuf CodedOutputStream ByteString)))

(defn protobuf-load [data] 5)

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
    :fixed32 (CodedOutputStream/computeFixed32Size tag value)
    :fixed64 (CodedOutputStream/computeFixed64Size tag value)
    :bool (CodedOutputStream/computeBoolSize tag value)
    :string (CodedOutputStream/computeStringSize tag value)
    :bytes (CodedOutputStream/computeBytesSize tag (ByteString/copyFrom value))
    ))

(defn protobuf-compute-size
  [schema message]
  (let [message-name (:message (meta message))
        message-schema (some #(when
                                (= message-name (:name %))
                                (:content %))
                             schema)]
    (reduce +
            (map (fn [x]
                   (let [attribute-name (name x)
                         attribute-schema (some #(when (= attribute-name (:name %)) %) message-schema)]
                     (protobuf-compute-attribute-size (:type attribute-schema) (:tag attribute-schema) ((keyword attribute-name) message))))
                 (keys message)))))

(defn protobuf-dump-attribute
  [type tag value stream]
  (case type
    :double (.writeDouble stream tag value)
    :float (.writeFloat stream tag value)
    :int32 (.writeInt32 stream tag value)
    :int64 (.writeInt64 stream tag value)
    :uint32 (.writeUInt32 stream tag value)
    :uint64 (.writeUInt64 stream tag value)
    :sint32 (.writeSInt32 stream tag value)
    :sint64 (.writeSInt64 stream tag value)
    :fixed32 (.writeFixed32 stream tag value)
    :fixed64 (.writeFixed64 stream tag value)
    :bool (.writeBool stream tag value)
    :string (.writeString stream tag value)
    :bytes (.writeBytes stream tag (ByteString/copyFrom value))
    ))

(defn protobuf-dump-stream
  [schema message stream]
  (let [message-name (:message (meta message))
        message-schema (some #(when
                                (= message-name (:name %))
                                (:content %))
                             schema)]
    (doall (map (fn [x]
           (let [attribute-name (name x)
                 attribute-schema (some #(when (= attribute-name (:name %)) %) message-schema)]
             (protobuf-dump-attribute (:type attribute-schema) (:tag attribute-schema) ((keyword attribute-name) message) stream)))
         (keys message)))))

(defn protobuf-dump
  [schema message]
  (let [buffer (byte-array (protobuf-compute-size schema message))
        output-stream (CodedOutputStream/newInstance buffer)]
    (do
      (protobuf-dump-stream schema message output-stream)
      (seq buffer))))
