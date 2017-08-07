(ns material-cljs.react.elevation
  (:require
    [clojure.spec.alpha :as s]
    [material-cljs.utils :as u]
    [material-cljs.react-wrapper.core :as w]
    [material-cljs.dom-helpers :as dom]))


(def default-props
  {:z 1
   :element dom/div})

(defn make-elevation-class [z]
  (str "mdc-elevation--z" z))

(w/def-component Elevation
  (render [this]
    (let [{:keys [element z] :as props} (u/ensure-props (w/props this) default-props)]
      (u/render-container this element {:className (make-elevation-class z)
                                        ::u/props-filter #{:z :element}}))))



(s/def ::z (s/and int? #(< 0 % 25)))
(s/def ::element fn?)
(s/def ::elevation-props (s/keys :opt-un [::z ::element]))


(w/def-constructor mdc-elevation Elevation :spec ::elevation-props)
