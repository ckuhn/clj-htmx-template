(ns clj-htmx-template.dev
  (:require [clj-htmx-template.server :as server]
            [clj-htmx-template.css :as css]
            [mount.core :as mount :refer [defstate]])
  (:import (org.eclipse.jetty.server Server)))

(defn- dev-handler []
  (fn
    ([req] ((server/handler) req))
    ([req resp raise] ((server/handler) req resp raise))))

(defstate ^Server dev-server
  :start (server/server dev-handler)
  :stop (.stop dev-server))

(defstate watch-styles
  :start (css/watch-all-styles)
  :stop (css/remove-all-watches watch-styles))

(defn -main [] (mount/start))

(comment (-main)
         (mount/stop))
