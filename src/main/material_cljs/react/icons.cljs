(ns material-cljs.react.icons
  (:require [goog.object :as o]
            [clojure.spec.alpha :as s]
            [material-cljs.utils :as u]
            [material-cljs.react-wrapper.core :as w]
            [material-cljs.dom-helpers :as dom]))


;; Styles from http://google.github.io/material-design-icons/
;; section "Styling icons in material design"

(def default-styles
  "
  /* Rules for sizing the icon. */
  .material-icons.md-18 { font-size: 18px; }
  .material-icons.md-24 { font-size: 24px; }
  .material-icons.md-36 { font-size: 36px; }
  .material-icons.md-48 { font-size: 48px; }

  /* Rules for using icons as black on a light background. */
  .material-icons.md-dark { color: rgba(0, 0, 0, 0.54); }
  .material-icons.md-dark.md-inactive { color: rgba(0, 0, 0, 0.26); }

  /* Rules for using icons as white on a dark background. */
  .material-icons.md-light { color: rgba(255, 255, 255, 1); }
  .material-icons.md-light.md-inactive { color: rgba(255, 255, 255, 0.3); }
  ")

(defn mdc-icons-font-styles []
  (dom/style {:key "mdcIconsFontStyles"} default-styles))



(s/def ::element fn?)
(s/def ::size #{18 24 36 48})
(s/def ::dark boolean?)
(s/def ::light boolean?)
(s/def ::inactive boolean?)

(s/def ::font-icon-props (s/keys :opt-un [::element ::size ::dark ::light ::inactive]))

(def default-font-icon-props
  {:element dom/span
   :size 18
   :dark false
   :light false
   :inactive false})

(def font-icon-props->mdc-classes
  {:size #(case %
            18 "md-18"
            24 "md-24"
            36 "md-36"
            48 "md-48"
            "md-18")

   :dark (constantly "md-dark")
   :light (constantly "md-light")
   :inactive (constantly "md-inactive")})


(w/def-component GoogleFontIcon
  (render [this]
    (let [props (u/ensure-props (w/props this) default-font-icon-props)]
      (u/render-container this (:element props) {:className (u/mdc-classes "material-icons"
                                                              (u/mdc-classes-from-props props font-icon-props->mdc-classes))
                                                 ::u/props-filter (keys default-font-icon-props)}))))


(w/def-constructor mdc-g-font-icon GoogleFontIcon :spec ::font-icon-props)