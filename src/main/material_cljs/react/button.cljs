(ns material-cljs.react.button
  (:require
    [material-cljs.react-wrapper.core :as w]
    [material-cljs.utils :as u]
    [material-cljs.dom-helpers :as dom]))



;; TODO: adding ripples

(def mdc-button-props [:dense :raised :compact :primary :accented])

(def props->mdc-class
  (zipmap mdc-button-props
          (map (partial constantly) ["mdc-button--dense"
                                     "mdc-button--raised"
                                     "mdc-button--compact"
                                     "mdc-button--primary"
                                     "mdc-button--accent"])))

(w/def-component Button
  (render [this]
    (let [props (w/props this)
          button-props (u/remove-props props mdc-button-props)
          class-names (if-let [cs (:className props)] #{cs} #{})]
      (dom/button (assoc button-props
                    :className (u/mdc-classes
                                 "mdc-button"
                                 class-names
                                 (u/mdc-classes-from-props props props->mdc-class)))
                  (w/children this)))))

(def mdc-button (w/factory Button))