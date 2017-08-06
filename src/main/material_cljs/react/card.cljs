(ns material-cljs.react.card
  (:require
    [material-cljs.utils :as u]
    [material-cljs.react-wrapper.core :as w]
    [material-cljs.dom-helpers :as dom]
    [material-cljs.react.button :refer [mdc-button]]))

;; TODO: handle media items - Some sort of wrappper component or just a function adding classes to props might be enough
;; TODO: handle horizontal layout
(w/def-component Card
  (render [this]
    (u/render-container this dom/div {:className "mdc-card"})))

(def mdc-card (w/factory Card))

(w/def-component CardHeader
  (render [this]
    (u/render-container this dom/section {:className "mdc-card__primary"})))

(def mdc-card-header (w/factory CardHeader))


(defn card-title-classes [props]
  (u/mdc-classes
    "mdc-card__title"
    (u/mdc-classes-from-props props
      {:large-title (constantly "mdc-card__title--large")})))

(w/def-component CardTitle
  (render [this]
    (let [props (w/props this)]
      (u/render-container this dom/h1 {:className (card-title-classes props)}))))


(def mdc-card-title (w/factory CardTitle))

(w/def-component CardSubTitle
  (render [this]
    (u/render-container this dom/h2 {:className "mdc-card__subtitle"})))

(def mdc-card-sub-title (w/factory CardSubTitle))

(w/def-component CardText
  (render [this]
    (u/render-container this dom/section {:className "mdc-card__supporting-text"})))

(def mdc-card-text (w/factory CardText))


(w/def-component CardActions
  (render [this]
    (let [props (w/props this)]
      (u/render-container this dom/section {:className (if (:vertical props)
                                                         "mdc-card__actions mdc-card__actions--vertical"
                                                         "mdc-card__actions")}))))

(def mdc-card-actions (w/factory CardActions))


(w/def-component CardActionButton
  (render [this]
    (u/render-container this mdc-button {:className "mdc-button--compact mdc-card__action"})))

(def mdc-card-action-button (w/factory CardActionButton))