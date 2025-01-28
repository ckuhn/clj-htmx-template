(ns clj-htmx-template.core
  (:require [clj-htmx-template.server :as server]
            [mount.core :as mount :refer [defstate]])
  (:import (org.eclipse.jetty.server Server)))

(defstate ^Server web-server :start (server/server) :stop (.stop web-server))

(defn -main [& _] (mount/start))

(comment (-main)
         (mount/start)
         (mount/stop))
