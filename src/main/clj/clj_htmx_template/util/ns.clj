(ns clj-htmx-template.util.ns
  (:require [clojure.string :as str]))

(defn all-app-ns [] (->> (all-ns) (filter #(str/starts-with? % "clj-htmx-template"))))

(defn all-app-publics
  ([] (->> (all-app-ns) (map #(-> % ns-publics vals)) flatten))
  ([meta-filter-key] (->> (all-app-publics) (filter #(-> % meta meta-filter-key)))))
