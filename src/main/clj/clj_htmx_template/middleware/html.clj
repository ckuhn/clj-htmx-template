(ns clj-htmx-template.middleware.html
  (:require [hiccup2.core :as hiccup]
            [clj-htmx-template.index :as index]
            [ring.util.response :as r]))

(defn- write-body-as-html [resp req]
  (if (and (r/get-header req "HX-Request") (not (r/get-header req "HX-Boosted")))
    (update resp :body #(-> % hiccup/html str))
    (update resp :body (partial index/index req))))

(defn wrap-html-response [req resp]
  (if (r/get-header resp "Content-Type")
    resp
    (-> resp
        (write-body-as-html req)
        (r/header "Content-Type" "text/html"))))

(def html-middleware
  {:name        ::html-middleware
   :description "Set response Content-Type to text/html and render body as HTML tags using hiccup"
   :wrap        (fn [handler]
                  (fn
                    ([req]
                     (wrap-html-response req (handler req)))
                    ([req respond raise]
                     (handler req #(respond (wrap-html-response req %)) raise))))})
