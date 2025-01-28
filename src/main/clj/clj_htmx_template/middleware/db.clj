(ns clj-htmx-template.middleware.db
  (:require [datomic.client.api :as d]
            [clj-htmx-template.db.core :as db]))

(defn wrap-db-request [request]
  (-> request
      (assoc :db (d/db db/conn))))

(def db-middleware
  {:name        ::db-middleware
   :description "Add filtered db to the request object"
   :wrap        (fn [handler]
                  (fn
                    ([request]
                     (handler (wrap-db-request request)))
                    ([request respond raise]
                     (handler (wrap-db-request request) respond raise))))})
