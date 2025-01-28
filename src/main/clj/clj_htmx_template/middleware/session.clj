(ns clj-htmx-template.middleware.session
  (:require [ring.middleware.session :as ring]
            [ring.middleware.session.memory :as memory]))

(def session-store (memory/memory-store))

(def session-middleware
  {:name        ::session-middleware
   :description "Wrap requests with Ring session middleware"
   :wrap        (fn [handler] (ring/wrap-session handler {:store session-store}))})
