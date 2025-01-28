(ns clj-htmx-template.routes.home
  (:require [datomic.client.api :as d]
            [clj-htmx-template.spec.user :as user]
            [ring.util.response :as rr]))

(defn- count-users [db]
  (or (-> (d/q '[:find (count-distinct ?e) :where [?e ::user/sub _]] db)
          first
          first)
      0))

(defn home-handler [{:keys [db]}]
  (-> [:article
       [:header [:h1 "welcome"]]
       (str "clj-htmx-template currently has " (count-users db) " users. you could be next.")]

      rr/response))

(defn routes []
  ["/" {:get home-handler}])
