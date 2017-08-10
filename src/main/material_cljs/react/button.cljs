(ns material-cljs.react.button
  (:require
    [clojure.spec.alpha :as s]
    [material-cljs.react-wrapper.core :as w]
    [material-cljs.utils :as u]
    [material-cljs.dom-helpers :as dom]))



;; TODO: adding ripples




(s/def ::dense boolean?)
(s/def ::raised boolean?)
(s/def ::compact boolean?)
(s/def ::primary boolean?)
(s/def ::accented boolean?)

(s/def ::mdc-button-props (s/keys :opt-un [::dense ::raised ::compact ::primary ::accented]))

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
    (let [props (w/props this)]
      (u/render-container this dom/button {:className (u/mdc-classes
                                                        "mdc-button"
                                                        (u/mdc-classes-from-props props props->mdc-class))
                                           ::u/props-filter mdc-button-props}))))

(w/def-constructor mdc-button Button :spec ::mdc-button-props)

;; ---------------------------------------------------------------------------------------------------------------------
(s/def ::mini boolean?)
(s/def ::plain boolean?)

(s/def ::fab-props (s/keys :opt-un [::mini ::plain]))

(def fab-props->icons-mdc-classes
  {:mini (constantly "mdc-fab--mini")
   :plain (constantly "mdc-fab--plain")})

(defn process-icon-child [props react-icon-component]
  (let [mc-props (w/props react-icon-component)
        passed-css-classes (:className mc-props "")
        new-props (assoc mc-props
                    :className
                    (u/join-classes [passed-css-classes "mdc-fab__icon"]))]
    (w/react-clone-element react-icon-component
                           (w/wrap new-props)
                           (w/children react-icon-component))))

(w/def-component Fab
  (render [this]
    (let [props (w/props this)]
      (u/render-container this dom/button
                          {:className (u/mdc-classes
                                        "mdc-fab"
                                        (u/mdc-classes-from-props props
                                          fab-props->icons-mdc-classes))
                           ::u/props-filter [:mini :plain]
                           ::u/children-processor #(process-icon-child props %)}))))



(w/def-constructor mdc-fab Fab :spec ::fab-props)


;; TODO: Icon toogle