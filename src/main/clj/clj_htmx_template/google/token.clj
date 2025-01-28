(ns clj-htmx-template.google.token
  (:require [clj-htmx-template.config :as config])
  (:import (com.google.api.client.googleapis.auth.oauth2 GoogleIdToken GoogleIdTokenVerifier GoogleIdTokenVerifier$Builder)
           (com.google.api.client.googleapis.javanet GoogleNetHttpTransport)
           (com.google.api.client.http HttpTransport)
           (com.google.api.client.json JsonFactory)
           (com.google.api.client.json.gson GsonFactory)
           (java.util Collections)))

(def ^HttpTransport http-transport (GoogleNetHttpTransport/newTrustedTransport))
(def ^JsonFactory json-factory (GsonFactory/getDefaultInstance))

(def ^GoogleIdTokenVerifier token-verifier
  (-> (doto (GoogleIdTokenVerifier$Builder. http-transport json-factory)
        (.setAudience (Collections/singletonList (config/oauth-google-client-id))))
      (.build)))

(defn verify-token [^String token]
  (when-let [id-token (.verify token-verifier token)]
    (-> id-token
        .getPayload
        .entrySet
        (->> (into {}))
        (update-keys keyword))))
