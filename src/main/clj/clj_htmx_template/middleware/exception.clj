(ns clj-htmx-template.middleware.exception
  (:require [clojure.spec.alpha :as s]
            [reitit.ring.middleware.exception :as exception]
            [reitit.coercion :as coercion]))

(defn- default-handler [ex _]
  {:status 500
   :body   [:div
            [:h1 "500 Server Error"]
            [:p (ex-message ex)]]})

(def exception-middleware
  (exception/create-exception-middleware
    {::exception/default default-handler
     ::exception/wrap    (fn [handler e request]
                           (do (if (= (-> e ex-data :type) ::coercion/request-coercion)
                                 (println (with-out-str (-> e ex-data :problems s/explain-out)))
                                 (.printStackTrace e))
                               (handler e request)))}))
