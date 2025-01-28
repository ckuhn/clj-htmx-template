(ns clj-htmx-template.db.core
  (:require [datomic.client.api :as d]
            [clj-htmx-template.config :as config]
            [clj-htmx-template.db.schema :as schema]
            [mount.core :refer [defstate]]))

(defstate client :start (d/client (config/datomic-client)))

(defstate conn :start (let [db-args (config/datomic-db)]
                        (d/create-database client db-args)
                        (d/connect client db-args)))

(defstate schema :start (d/transact conn {:tx-data (schema/schema)}))
