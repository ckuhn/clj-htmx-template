(ns clj-htmx-template.routes.auth
  (:require [clojure.spec.alpha :as s]
            [datomic.client.api :as d]
            [clj-htmx-template.db.core :as db]
            [clj-htmx-template.google.token :as oauth]
            [clj-htmx-template.spec.core :as spec]
            [clj-htmx-template.spec.user :as user]
            [ring.util.response :as r]
            [ring.util.response :as rr]))

(s/def ::credential spec/non-blank-str?)
(s/def ::g_csrf_token spec/non-blank-str?)
(s/def ::google-params (s/keys :req-un [::credential ::g_csrf_token]))

(defn- save-latest-user-data [payload]
  (d/transact db/conn {:tx-data [{::user/sub   (:sub payload)
                                  ::user/name  (:name payload)
                                  ::user/email (:email payload)}]}))

(defn- post-google-callback [{:keys [parameters cookies]}]
  (if (not= (-> cookies (get "g_csrf_token") :value) (-> parameters :form :g_csrf_token))
    (rr/bad-request "Invalid CSRF Token")
    (let [token (-> parameters :form :credential)
          payload (oauth/verify-token token)
          identity (assoc payload :token token)]

      (save-latest-user-data payload)

      (-> (rr/redirect "/")
          (assoc-in [:session :identity] identity)))))

(defn- get-signout [_]
  (-> (r/redirect "/")
      (assoc :session {})))

(defn routes []
  [["/google" {:post {:parameters {:form ::google-params}
                      :handler    post-google-callback}}]

   ["/signout" {:get get-signout}]])
