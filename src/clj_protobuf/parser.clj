(ns clj-protobuf.parser
  (:require [instaparse.core :as insta]))

(def protobuf-parser
  (insta/parser "resources/proto.ebnf" :auto-whitespace :standard))
