(ns clj-htmx-template.db.schema
  (:require [clj-htmx-template.util.ns :as ns]
            [provisdom.spectomic.core :as spectomic]))

(defn schema [] (->> (ns/all-app-publics :schema)
                     (map var-get)
                     (apply concat)
                     spectomic/datomic-schema))
