(ns clj-protobuf.test.schemas)

(def schema-trivial
  [{:type :message
    :name "Person"
    :content [{:label :required :type :int32 :name "age" :tag 1}]}])

(def schema-simple
  [{:name "Person"
    :type :message
    :content [{:label :required :type :string :name "name" :tag 1}
              {:label :required :type :int32 :name "age" :tag 2}
              {:label :required :type :string :name "email" :tag 3}]}])

(def schema-intermediate
  [{:name "Person"
    :type :message
    :content [{:label :required :type :string :name "name" :tag 1}
              {:label :required :type :int32 :name "age" :tag 2}
              {:label :required :type :string :name "email" :tag 3}
              {:label :optional :type :enum :name "personType" :tag 4}
              {:name "PersonType" :type :enum
               :content [{:name "CUSTOMER" :tag 1}
                         {:name "PROSPECT" :tag 2}]}]}])

(def schema-advanced
  [{:type :message
    :name "Person"
    :content [{:label :required :name "name" :type :string :tag 1}
              {:label :required :name "age" :type :int32 :tag 2}
              {:label :required :name "email" :type :string :tag 3}
              {:name "Phone" :type :message
               :content [{:name "PhoneType" :type :enum
                          :content [{:name "MOBILE" :tag 1}
                                    {:name "WORK" :tag 2}
                                    {:name "HOME" :tag 3}]}
                         {:label :required :name "number" :type :string :tag 1}
                         {:label :required :name "type" :type "PhoneType" :tag 2}]}
              {:label :repeated :name "phone" :type "Phone" :tag 4}]}
   {:type :message
    :name "AnyMessage"
    :content [{:label :required :name "anystring" :type :string :tag 1}]}
   ])
