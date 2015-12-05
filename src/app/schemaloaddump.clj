(ns app.schemaloaddump
  (:use [app.parser]))

(defn tab [n]
  (apply str (repeat n "\t")))

(defn protobuf-schema-dump
  ([schema] (protobuf-schema-dump schema 0))
  ([schema indent] (clojure.string/join
  (for [item schema]
    (cond
      (= (item :type) :message) (format "%smessage %s {\n%s%s}\n" (tab indent) (item :name) (protobuf-schema-dump (item :content) (inc indent)) (tab indent))
      (= (item :type) :enum) (format "%senum %s {\n%s%s}\n" (tab indent) (item :name) (clojure.string/join (for [enumitem (item :content)]
        (format "%s%s = %s;\n" (tab (inc indent)) (enumitem :name) (enumitem :tag)))) (tab indent))
      :else (format "%s%s %s %s = %s;\n" (tab indent) (name (item :label)) (name (item :type)) (item :name) (item :tag)))))))

(defn protobuf-schema-load [schema]
  (letfn [
    (parse-int [value] (read-string value))
    (convert-type [type] (if (vector? type) (type 1) (keyword type)))
    (convert [schema] (cond
      (= (schema 0) :proto) (vec (for [x (rest schema)] (convert x)))
      (= (schema 0) :message) {:name (schema 1) :type :message :content (vec (for [x (drop 2 schema)] (convert x)))}
      (= (schema 0) :field) {:label (keyword (schema 1)) :type (convert-type (schema 2)) :name (schema 3) :tag (parse-int (schema 4))}
      (= (schema 0) :enum) {:name (schema 1) :type :enum :content (vec (for [x (drop 2 schema)] (convert x)))}
      (= (schema 0) :enumField) {:name (schema 1) :tag (parse-int (schema 2))}
      :else (schema 0)))]
    (convert (protobuf-parser schema))))
