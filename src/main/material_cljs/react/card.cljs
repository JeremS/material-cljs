(ns material-cljs.react.card
  (:require
    [clojure.spec.alpha :as s]
    [material-cljs.utils :as u]
    [material-cljs.react-wrapper.core :as w]
    [material-cljs.dom-helpers :as dom]
    [material-cljs.react.button :refer [mdc-button]]))

;; TODO: Try to fix the display of supporting text in horizontal cards
;; ---------------------------------------------------------------------------------------------------------------------
(defn card-classes [props]
  (u/mdc-classes
    "mdc-card"
    (u/mdc-classes-from-props props
      {:horizontal (constantly "mdc-card__horizontal-block")})))

(w/def-component Card
  (render [this]
    (let [props (w/props this)]
      (u/render-container this dom/div {:className (card-classes props)
                                        ::u/props-filter [:horizontal]}))))

(s/def ::horizontal boolean?)
(s/def ::card-props (s/keys :opt-un [::horizontal]))


(w/def-constructor mdc-card Card :spec ::card-props)


;; ---------------------------------------------------------------------------------------------------------------------
(w/def-component CardHeader
  (render [this]
    (u/render-container this dom/section {:className "mdc-card__primary"})))

(w/def-constructor mdc-card-header CardHeader)



;; ---------------------------------------------------------------------------------------------------------------------
(defn card-title-classes [props]
  (u/mdc-classes
    "mdc-card__title"
    (u/mdc-classes-from-props props
      {:large-title (constantly "mdc-card__title--large")})))

(w/def-component CardTitle
  (render [this]
    (let [props (w/props this)]
      (u/render-container this dom/h1 {:className (card-title-classes props)
                                       ::u/props-filter [:large-title]}))))

(s/def ::large-title boolean?)
(s/def ::card-title-props (s/keys :opt-un [::large-title]))

(w/def-constructor mdc-card-title CardTitle :spec ::card-title-props)

;; ---------------------------------------------------------------------------------------------------------------------
(w/def-component CardSubTitle
  (render [this]
    (u/render-container this dom/h2 {:className "mdc-card__subtitle"})))

(w/def-constructor mdc-card-sub-title CardSubTitle)

;; ---------------------------------------------------------------------------------------------------------------------
(w/def-component CardText
  (render [this]
    (u/render-container this dom/section {:className "mdc-card__supporting-text"})))

(w/def-constructor mdc-card-text CardText)


;; ---------------------------------------------------------------------------------------------------------------------
(defn card-action-classes [props]
  (u/mdc-classes
    "mdc-card__actions"
    (u/mdc-classes-from-props props
      {:vertical (constantly "mdc-card__actions--vertical")})))


(w/def-component CardActions
  (render [this]
    (let [props (w/props this)]
      (u/render-container this dom/section {:className (card-action-classes props)
                                            ::u/props-filter [:vertical]}))))

(s/def ::vertical boolean?)
(s/def ::card-actions-props (s/keys :opt-un [::vertical]))

(w/def-constructor mdc-card-actions CardActions :spec ::card-actions-props)

;; ---------------------------------------------------------------------------------------------------------------------
(w/def-component CardActionButton
  (render [this]
    (u/render-container this mdc-button {:className "mdc-card__action"
                                         :compact true})))

(w/def-constructor mdc-card-action-button CardActionButton)

;; ---------------------------------------------------------------------------------------------------------------------
(def default-media-item-properties
  {:height :1})

(defn height->mdc-class [val]
  (letfn [(make-class [x]
            (str "mdc-card__media-item--" x "x"))]
    (case val
      :1   (make-class "1")
      :1.5 (make-class "1dot5x")
      :2   (make-class "2")
      :3   (make-class "3")
      (make-class "1"))))


(def props->mdc-class
  {:height height->mdc-class})

(defn card-media-classes [props]
  (u/mdc-classes
    "mdc-card__media-item"
    (u/mdc-classes-from-props props props->mdc-class)))

(w/def-component MediaItem
  (render [this]
    (let [props (u/ensure-props (w/props this) default-media-item-properties)
          element (:element props)]
      (u/render-container this element {:className (card-media-classes props)
                                        ::u/props-filter #{:height :element}}))))

(s/def ::element #(not (nil? %)))
(s/def ::height #{:1 :1.5 :2 :3})
(s/def ::card-media-item-props (s/keys :req-un [::element]
                                       :opt-un [::height]))

(w/def-constructor mdc-card-media-item MediaItem
  :spec ::card-media-item-props)