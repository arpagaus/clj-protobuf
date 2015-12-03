(println "Hello ProtoBuf")

(def advanced-schema {
 :Person [:message {
  :name [:required :string 1]
  :id [:required :int32 2]
  :email [:required :string 3]
  :Phone [:message {
   :PhoneType [:enum {
    :MOBILE 1
    :WORK 2
    :HOME 3
   } ]
   :number [:required :string 1]
   :type [:required :PhoneType 2]
  } ]
  :phone [:repeated :Phone 4]
 } ]
 :AnyMessage [:message {
  :anystring [:required :string 1]
 } ]
} )

(def simple-schema {
 :Person [:message {
  :age [:required :int32 1]
 } ]
} )

;;(def schema-single
;;  [{:type :message
;;    :name "Person"
;;    :content [{:type :int32 :name "age" :tag 1}]}])

(def advanced-schema2 [
  {:type :message :name "Person" :content [
    {:label :required :name "name" :type :string :tag 1}
    {:label :required :name "id" :type :int32 :tag 2}
    {:label :required :name "email" :type :string :tag 3}
    {:name "Phone" :type :message :content [
      {:name "PhoneType" :type :enum :content [
        {:name "MOBILE" :tag 1}
        {:name "WORK" :tag 2}
        {:name "HOME" :tag 3}
      ] }
      {:label :required :name "number" :type :string :tag 1}
      {:label :required :name "type" :type "PhoneType" :tag 2}
    ] }
    {:label :repeated :name "phone" :type "Phone" :tag 4}
  ] }
  {:type :message :name "AnyMessage" :content [
    {:label :required :name "anystring" :type :string :tag 1}
  ] }
])

(defn tab [n]
  (apply str (repeat n "\t")))

(defn protobuf-schema-dump2
  ([schema] (protobuf-schema-dump2 schema 0))
  ([schema indent] (clojure.string/join
  (for [item schema]
    (cond
      (= (item :type) :message) (format "%smessage %s {\n%s%s}\n" (tab indent) (item :name) (protobuf-schema-dump2 (item :content) (inc indent)) (tab indent))
      (= (item :type) :enum) (format "%senum %s {\n%s%s}\n" (tab indent) (item :name) (clojure.string/join
        (for [enumitem (item :content)] (format "%s%s = %s;\n" (tab (inc indent)) (enumitem :name) (enumitem :tag)))) (tab indent))
      :else (format "%s%s %s %s = %s;\n" (tab indent) (name (item :label)) (name (item :type)) (item :name) (item :tag)))))))

(println (protobuf-schema-dump2 advanced-schema2))


;; protobuf-schema-load string -> map
;; protobuf-schema-dump map -> string

(defn tab [n]
  (apply str (repeat n "\t")))

(defn protobuf-schema-dump
  ([schema] (protobuf-schema-dump schema 0))
  ([schema indent] (clojure.string/join
    (for [[k v] schema]
      (cond
       (or (= (v 0) :required) (= (v 0) :repeated)) (format "%s%s %s %s = %s;\n" (tab indent) (name (v 0)) (name (v 1)) (name k) (v 2))
       (= (v 0) :message) (format "%smessage %s {\n%s%s}\n" (tab indent) (name k) (protobuf-schema-dump (v 1) (inc indent)) (tab indent))
       (= (v 0) :enum) (format "%senum %s {\n%s%s}\n" (tab indent) (name k) (clojure.string/join
                                                                             (for [[enumk enumv] (v 1)]
                                                                               (format "%s%s = %s;\n" (tab (inc indent)) (name enumk) enumv))
                                                                             ) (tab indent))
       :else (format "Oops %s" k)
       )))))

(println (protobuf-schema-dump simple-schema))
(println (protobuf-schema-dump advanced-schema))

;; (spit "d:\\person.proto" (protobuf-schema-dump advanced-schema))

;; TODO https://github.com/Engelberg/instaparse
;; (protobuf-schema-load (slurp "person.txt"))

;; TODO multimap?
(def person {
 :name "Any One"
 :age 88
 :email "any.one@anywhere.com"
 :phone [ {
  :number "123456789"
  :type 1
 } {
  :number "987654321"
  :type 2
 } ]
} )

;; protobuf-dump object -> bytes
;; protobuf-load schema bytes -> object

;; protobuf-dump-string object -> string
;; protobuf-load-string string -> object

;; (protobuf-dump advanced-schema person)
;; (protobuf-load advanced-schema bytes)

;; (println advanced-schema)
;; (println person-instance)

