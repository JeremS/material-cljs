(ns material-cljs.react.card
  (:require
    [material-cljs.utils :as u]
    [material-cljs.react-wrapper.core :as w]
    [material-cljs.dom-helpers :as dom]
    [material-cljs.react.button :refer [mdc-button]]))

;; TODO: Spec components

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

(def mdc-card (w/factory Card))


;; ---------------------------------------------------------------------------------------------------------------------
(w/def-component CardHeader
  (render [this]
    (u/render-container this dom/section {:className "mdc-card__primary"})))

(def mdc-card-header (w/factory CardHeader))

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

(def mdc-card-title (w/factory CardTitle))


;; ---------------------------------------------------------------------------------------------------------------------
(w/def-component CardSubTitle
  (render [this]
    (u/render-container this dom/h2 {:className "mdc-card__subtitle"})))

(def mdc-card-sub-title (w/factory CardSubTitle))

;; ---------------------------------------------------------------------------------------------------------------------
(w/def-component CardText
  (render [this]
    (u/render-container this dom/section {:className "mdc-card__supporting-text"})))

(def mdc-card-text (w/factory CardText))


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

(def mdc-card-actions (w/factory CardActions))

;; ---------------------------------------------------------------------------------------------------------------------
(w/def-component CardActionButton
  (render [this]
    (u/render-container this mdc-button {:className "mdc-card__action"
                                         :compact true})))

(def mdc-card-action-button (w/factory CardActionButton))

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
                                        ::u/props-filter (keys default-media-item-properties)}))))

(def mdc-card-media-item (w/factory MediaItem))
