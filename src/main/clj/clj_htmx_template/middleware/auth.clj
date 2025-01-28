(ns clj-htmx-template.middleware.auth
  (:require [buddy.auth :as auth]
            [buddy.auth.backends :as backends]
            [buddy.auth.middleware :as mw]
            [clojure.spec.alpha :as s]
            [clj-htmx-template.google.token :as google]
            [ring.util.response :as rr]))

(defn unauthorized-handler
  [req _meta]
  (cond
    (auth/authenticated? req) {:status 403 :body [:h1 "403 Unauthorized"]}
    :else (rr/redirect "/")))

(defn authfn [data]
  (when (google/verify-token (:token data))
    data))

(def auth-backend (backends/session {:authfn               authfn
                                     :unauthorized-handler unauthorized-handler}))

(s/def ::protect boolean?)
(s/def ::spec (s/keys :opt-un [::protect]))

(def authentication-middleware
  {:name        ::authentication-middleware
   :spec        ::spec
   :description "Wrap with buddy auth if :protect true is present"
   :compile     (fn [route-data _opts]
                  (fn [handler]
                    (mw/wrap-authentication handler auth-backend)))})

(defn- wrap-authorization-request [handler]
  (fn [req]
    (if (not (auth/authenticated? req))
      (auth/throw-unauthorized)
      (handler req))))

(def authorization-middleware
  {:name        ::authorization-middleware
   :spec        ::spec
   :description "Verify that the user is authorized to access resources if :protect is present"
   :compile     (fn [route-data _opts]
                  (when (:protect route-data)
                    (fn [handler]
                      (mw/wrap-authorization (wrap-authorization-request handler) auth-backend))))})
