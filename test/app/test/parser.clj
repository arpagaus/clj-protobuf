(ns app.test.parser
  (:use [app.parser])
  (:use [clojure.test])
  (:require [instaparse.print :as instap]))

(deftest parsdef
  (do (println (protobuf-gramma "message Person {required int32 age = 1;}") )
    (is (.contains (instap/Parser->str protobuf-gramma) "hexEscape"))
    )
  )
