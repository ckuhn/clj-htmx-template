(ns clj-htmx-template.index
  (:require [hiccup.page :as hiccup]
            [clj-htmx-template.config :as config]
            [clj-htmx-template.css :as css]
            [tick.core :as t]))

(def ^::css/styles index-styles [[:.nav-profile [[:img {:max-width "35px" :border-radius "10px"}]
                                                 [:div {:display "flex" :flex-direction "column"}]]]
                                 [:#credential_picker_container {:color-scheme "light"}]])

(defn- get-login-uri [req uri]
  (str (-> req :scheme name) "://" (get-in req [:headers "host"]) uri))

(defn- google-button [req]
  [:ul [:li
        (hiccup/include-js "https://accounts.google.com/gsi/client")
        [:div#g_id_onload {:data-client_id   (config/oauth-google-client-id)
                           :data-login_uri   (get-login-uri req "/google")
                           :data-auto_select "true"
                           :data-itp_support "true"}]
        [:div.g_id_signin {:style               {:color-scheme "light"}
                           :data-type           "standard"
                           :data-width          "150"
                           :data-size           "medium"
                           :data-theme          "outline"
                           :data-text           "sign_in_with"
                           :data-shape          "rectangular"
                           :data-logo_alignment "left"}]]])

(defn- profile [{:keys [picture name email]}]
  [:ul.nav-profile
   [:li [:a.g_id_signout {:href "/signout"} "sign out"]]
   [:li [:img {:src picture}]]
   [:li [:div [:p.mb-0 name] [:small email]]]])

(defn- header [{:keys [session] :as req}]
  [:nav
   [:ul
    [:li [:strong "clj-htmx-template"]]
    [:li [:a {:href "/" :hx-boost "true"} "home"]]]

   (if-let [identity (:identity session)]
     (profile identity)
     (google-button req))])

(defn- footer []
  [:div {:style {:text-align "center"}}
   (str "Copyright " (-> t/now t/year) " Connor Kuhn. All rights reserved.")])

(defn index [req content]
  (hiccup/html5
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]

    [:link {:rel "icon", :type "image/png", :href "/favicon/favicon-96x96.png", :sizes "96x96"}]
    [:link {:rel "icon", :type "image/svg+xml", :href "/favicon/favicon.svg"}]
    [:link {:rel "shortcut icon", :href "/favicon/favicon.ico"}]
    [:link {:rel "apple-touch-icon", :sizes "180x180", :href "/favicon/apple-touch-icon.png"}]
    [:meta {:name "apple-mobile-web-app-title", :content "clj-htmx-template"}]
    [:link {:rel "manifest", :href "/favicon/site.webmanifest"}]

    (hiccup/include-js "https://unpkg.com/htmx.org@2.0.4")

    (hiccup/include-css "https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.min.css"
                        "https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.colors.min.css"
                        css/css-file-uri)

    [:body
     [:header {:class "container"} (header req)]
     [:main {:class "container"} content]
     [:footer {:class "container"} (footer)]]))
