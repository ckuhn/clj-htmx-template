(ns clj-htmx-template.config
  (:require [aero.core :as aero]
            [clojure.spec.alpha :as s]
            [clj-htmx-template.spec.core :as specs]))

; Server Port
(s/def ::port int?)

; Datomic
(s/def ::server-type #{:ion :cloud :peer-server :peer-client :dev-local :datomic-local :local})
(s/def ::system specs/non-blank-str?)
(s/def ::storage-dir string?)
(s/def ::client (s/keys :req-un [::server-type ::system ::storage-dir]))

(s/def ::db-name specs/non-blank-str?)
(s/def ::db (s/keys :req-un [::db-name]))
(s/def ::datomic (s/keys :req-un [::client ::db]))

; OAuth
(s/def ::client-id string?)
(s/def ::client-secret string?)
(s/def ::google (s/keys :req [::client-id ::client-secret]))

; Config
(s/def ::spec (s/keys :req [::port ::datomic ::google]))

(defn- load-config [profile]
  (let [config (aero/read-config (clojure.java.io/resource "config.edn")
                                 {:profile profile})]
    (if-let [expl-data (s/explain-data ::spec config)]
      (throw (AssertionError. (with-out-str (s/explain-out expl-data))))
      config)))

(def profile (or (System/getenv "APP_PROFILE") "dev"))

(def config (load-config profile))

(defn port [] (::port config))

(defn datomic-client [] (-> config ::datomic :client))

(defn datomic-db [] (-> config ::datomic :db))

(defn ^String oauth-google-client-id [] (-> config ::google ::client-id))
