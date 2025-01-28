(ns clj-htmx-template.server
  (:require [clj-htmx-template.config :as config]
            [clj-htmx-template.middleware.auth :as auth]
            [clj-htmx-template.middleware.db :as db]
            [clj-htmx-template.middleware.exception :as exception]
            [clj-htmx-template.middleware.html :as html]
            [clj-htmx-template.middleware.session :as session]
            [clj-htmx-template.routes.core :as routes]
            [reitit.coercion.spec :as rcs]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as rrc]
            [reitit.ring.middleware.parameters :as rrmp]
            [ring.adapter.jetty :as jetty])
  (:import (org.eclipse.jetty.server Server)))

(defn router
  ([] (router routes/routes))
  ([routes]
   (ring/router
     (routes)
     {
      ;:reitit.middleware/transform rrmd/print-request-diffs
      :data {:coercion   rcs/coercion
             :middleware [html/html-middleware
                          exception/exception-middleware
                          rrmp/parameters-middleware
                          rrc/coerce-request-middleware
                          auth/authentication-middleware
                          auth/authorization-middleware
                          db/db-middleware]}})))

(defn handler
  ([] (handler router))
  ([router] (ring/ring-handler
              (router)
              (ring/routes
                (ring/create-resource-handler {:path "/"})
                (ring/create-default-handler
                  {:not-found (fn [req] (html/wrap-html-response req
                                                                 {:status 404
                                                                  :body   [:h1 "404 Not Found"]}))}))
              {:middleware [session/session-middleware]})))

(defn ^Server server
  ([] (server handler))
  ([handler] (jetty/run-jetty (handler) {:port (config/port) :join? false})))
