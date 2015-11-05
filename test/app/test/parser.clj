(ns app.test.parser
  (:use [app.parser])
  (:use [clojure.test])
  (:require [instaparse.print :as instap]))

(deftest parsdef
  (is  (.contains  ( instap/Parser->str protobuf-gramma ) "hexEscape"  ))
  )
