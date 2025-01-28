(ns clj-htmx-template.spec.core
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]))

(def non-blank-str? (s/and string? (complement str/blank?)))
