(ns material-cljs.react.grid
  (:require
    [material-cljs.utils :as u]
    [material-cljs.react-wrapper.core :as w]
    [material-cljs.dom-helpers :as dom]))

;; TODO: Spec components

(w/def-component Grid
  (render [this]
          (u/render-container this dom/div {:className "mdc-layout-grid"})))

(def mdc-grid (w/factory Grid))


(w/def-component GridInner
  (render [this]
    (u/render-container this dom/div {:className "mdc-layout-grid__inner"})))

(def mdc-grid-inner (w/factory GridInner))



(def make-span-class (partial u/make-mdc-class "mdc-layout-grid__cell--span-"))
(def make-order-class (partial u/make-mdc-class "mdc-layout-grid__cell--order-"))
(def make-alignment-class (partial u/make-mdc-class "mdc-layout-grid__cell--align-"))

(defn alignment-suffix [k]
  (case k
    :top "top"
    :middle "middle"
    :bottom "bottom"
    ""))

(def cell-props->mdc-classes
  {:span make-span-class
   :span-tablet #(make-span-class % "-tablet")
   :span-phone #(make-span-class % "-phone")
   :order make-order-class
   :align #(make-alignment-class (alignment-suffix %))})

(defn cell-classes [props]
  (u/mdc-classes
    "mdc-layout-grid__cell"
    (u/mdc-classes-from-props props cell-props->mdc-classes)))

(w/def-component GridCell
  (render [this]
    (let [props (w/props this)]
      (u/render-container this dom/div {:className (cell-classes props)
                                        ::u/props-filter (keys cell-props->mdc-classes)}))))

(def mdc-grid-cell (w/factory GridCell))