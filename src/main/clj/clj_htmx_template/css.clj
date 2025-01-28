(ns clj-htmx-template.css
  (:require [garden.core :as garden]
            [clj-htmx-template.util.ns :as ns]))

(def ^::styles global-styles
  [[":root" {:--pico-font-family "monospace"}]
   [:.mb-0 {:margin-bottom 0}]])

(def css-file-uri "css/clj-htmx-template.css")

(defn- find-all-styles-vars [] (ns/all-app-publics ::styles))

(defn write-all-styles []
  (->> (find-all-styles-vars)
       (map var-get)
       concat
       garden/css
       (spit (str "src/main/resources/public/" css-file-uri))))

(defn- style-watcher [_ _ _ _] (write-all-styles))

(defn watch-all-styles []
  (do (write-all-styles)
      (doseq [ref (find-all-styles-vars)]
        (add-watch ref :style-watcher style-watcher))))

(defn remove-all-watches [watches]
  (doseq [ref (find-all-styles-vars)]
    (remove-watch ref :style-watcher)))
