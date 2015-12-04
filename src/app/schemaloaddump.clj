(ns app.schemaloaddump)

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
