
(def schema-single
  [{:type :message
    :name "Person"
    :content [{:type :int32 :name "age" :tag 1}]}])

(def schema-advanced [
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
    {:label :required :type :string :tag 1}
  ] }
])
