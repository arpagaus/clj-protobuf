(ns app.parser
  (:require [instaparse.core :as insta]))



(def as-and-bs
  (insta/parser
    "proto.ebnf"))
