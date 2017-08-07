(ns material-cljs.react.elevation
  (:require
    [material-cljs.utils :as u]
    [material-cljs.react-wrapper.core :as w]
    [material-cljs.dom-helpers :as dom]))

;; TODO: Spec components


(def default-props
  {:z 1
   :element dom/div})

(defn make-elevation-class [z]
  (str "mdc-elevation--z" z))

(w/def-component Elevation
  (render [this]
    (let [{:keys [element z] :as props} (u/ensure-props (w/props this) default-props)
          pass-down-props (u/remove-props props [:z :element])
          css-classes (u/merge-classes (make-elevation-class z)
                                       (:className pass-down-props ""))]
      (element (assoc pass-down-props :className css-classes)
               (w/children this)))))

(def mdc-elevation (w/factory Elevation))