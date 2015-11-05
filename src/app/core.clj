(ns app.core)

(defn protobuf-load [data] 5)

(defn protobuf-dump
      [m]
      (seq (byte-array [(unchecked-byte 0x08)
                   (unchecked-byte 0x96)
                   (unchecked-byte 0x01)])))
