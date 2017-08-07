(ns material-cljs.react.grid
  (:require
    [clojure.spec.alpha :as s]
    [material-cljs.utils :as u]
    [material-cljs.react-wrapper.core :as w]
    [material-cljs.dom-helpers :as dom]))

;; ---------------------------------------------------------------------------------------------------------------------
(def grid-props->mdc-classes
  {:fixed (constantly "mdc-layout-grid--fixed-column-width")
   :grid-align (fn [p]
                 (case p
                   :left "mdc-layout-grid--align-left"
                   :right "mdc-layout-grid--align-right"
                   ""))})

(w/def-component Grid
  (render [this]
    (let [props (w/props this)]
      (u/render-container this dom/div {:className (u/mdc-classes
                                                     "mdc-layout-grid"
                                                     (u/mdc-classes-from-props props grid-props->mdc-classes))
                                        ::u/props-filter [:grid-align :fixed]}))))
(s/def ::fixed boolean?)
(s/def ::grid-align #{:left :right})

(s/def ::grid-props (s/keys :opt-un [::fixed ::grid-align]))

(w/def-constructor mdc-grid Grid :spec ::grid-props)

;; ---------------------------------------------------------------------------------------------------------------------
(w/def-component GridInner
  (render [this]
    (u/render-container this dom/div {:className "mdc-layout-grid__inner"})))

(w/def-constructor mdc-grid-inner GridInner)


;; ---------------------------------------------------------------------------------------------------------------------
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

(s/def ::span (s/and int? #(< 0 % 13)))
(s/def ::span-tablet (s/and int? #(< 0 % 13)))
(s/def ::span-phone (s/and int? #(< 0 % 13)))
(s/def ::order (s/and int? #(< 0 % 13)))
(s/def ::align #{:top :middle :bottom})

(s/def ::grid-cell-props (s/keys :opt-un [::span ::span-phone ::span-tablet ::order ::align]))


(w/def-constructor mdc-grid-cell GridCell :spec ::grid-cell-props)