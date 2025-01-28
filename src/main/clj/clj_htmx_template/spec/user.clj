(ns clj-htmx-template.spec.user
  (:require [clojure.spec.alpha :as s]
            [clj-htmx-template.spec.core :as core]))

(s/def ::sub core/non-blank-str?)
(s/def ::name core/non-blank-str?)
(s/def ::email core/non-blank-str?)

(s/def ::user (s/keys :req [::sub ::name ::email]))

(def ^:schema user-schema
  [[::sub {:db/unique :db.unique/identity}]
   ::name
   ::email])
