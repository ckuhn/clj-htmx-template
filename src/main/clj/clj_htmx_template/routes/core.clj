(ns clj-htmx-template.routes.core
  (:require [clj-htmx-template.routes.auth :as auth]
            [clj-htmx-template.routes.home :as home]))

(defn routes []
  [(home/routes)
   (auth/routes)])
