(ns app.parser
  (:require [instaparse.core :as insta]))



(def protobuf-gramma
  (insta/parser "resources/proto.ebnf" :auto-whitespace :standard ))
