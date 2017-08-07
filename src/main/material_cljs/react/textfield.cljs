(ns material-cljs.react.textfield
  (:require
    [goog.object :as o]
    [clojure.spec.alpha :as s]
    [material-cljs.utils :as u]
    [material-cljs.react-wrapper.core :as w]
    [material-cljs.dom-helpers :as dom]
    [material-cljs.react.impl.adapters :as adapters]
    ["@material/textfield/foundation" :as tfield-f]))

(def foundation-c (o/get tfield-f "default"))

(def textfield-f "textfieldFoundation_")
(def input-div "inputDiv_")
(def input "input_")
(def label "label_")
(def help-text "helpText_")

(def cleanups [textfield-f input-div input label help-text])

(defn make-text-field-adapter [this]
  (adapters/make-adapter
    adapters/css-classes-mapping
    (adapters/classes-management this :root-classes)

    {:add "addClassToLabel"
     :remove "removeClassFromLabel"}
    (adapters/classes-management this :label-classes)

    {:add "addClassToHelptext"
     :remove "addClassToHelptext"
     :has? "helptextHasClass"}
    (adapters/classes-management this :help-text-classes)

    {:set-attr "setHelptextAttr"
     :remove-attr "removeHelptextAttr"}
    (adapters/attrs-management this :help-text-attributes)

    {:register "registerInputFocusHandler"
     :deregister "deregisterInputFocusHandler"}
    (adapters/event-management this input dom/focus-ev)

    {:register "registerInputBlurHandler"
     :deregister "deregisterInputBlurHandler"}
    (adapters/event-management this input dom/blur-ev)

    {:register "registerInputInputHandler"
     :deregister "deregisterInputInputHandler"}
    (adapters/event-management this input dom/input-ev)

    {:register "registerInputKeydownHandler"
     :deregister "deregisterInputKeydownHandler"}
    (adapters/event-management this input dom/keydown-ev)

    {:get-native-input "getNativeInput"}
    (adapters/native-input-management this input)))




(def defaultprops
  {:name ""
   :label ""
   :help-text ""
   :className ""
   :multiline false
   :help-text-persistent false
   :textbox false
   :full-width false})

(def text-field-props (set (keys defaultprops)))

(defn ensure-name [props]
  (let [name (:name props)
        id (:id props)]
    (cond-> props
            (not (seq name)) (assoc :name (str "tfield-" id)))))

(defn ensure-textfield-props [props]
  (-> props
      (u/ensure-props defaultprops)
      (ensure-name)))


;; TODO: maybe handle input value see goog.dom.selection to handle cursor position
;; TODO: handle validation
;; TODO: handle onChange on input


(defn textfield-input [c props]
  (let [ctor (if (:multiline props) dom/textarea dom/input)
        input-props (u/remove-props props text-field-props)]
    (ctor (cond-> {:key "input"
                   :type "text"
                   :className "mdc-textfield__input"
                   :name (:name props)
                   :ref #(o/set c input %)}

                  (:full-width props) (assoc :placeholder (:label props))

                  true (merge input-props)))))


(defn text-field-label [c props state]
  (dom/label {:key "label"
              :htmlFor (:id props)
              :className (u/mdc-classes
                           "mdc-textfield__label"
                           (:label-classes state))
              :ref #(o/set c label %)}
             (:label props)))

(defn help-text-css [props state]
  (u/mdc-classes
    "mdc-textfield-helptext"
    (:help-text-classes state)
    (u/mdc-classes-from-props props
      {:help-text-persistent (constantly "mdc-textfield-helptext--persistent")})))

(defn text-field-help-text [c props state]
  (dom/p (merge {:key "help-text"
                 :className (help-text-css props state)
                 :ref #(o/set c help-text %)}
                (:help-text-attributes state))
         (:help-text props)))


(defn input-block-css [props state]
  (u/mdc-classes
    "mdc-textfield"
    (:root-classes state)
    (u/mdc-classes-from-props props
      {:disabled (constantly "mdc-textfield--disabled")
       :full-width (constantly "mdc-textfield--fullwidth")
       :textbox (constantly "mdc-textfield--box")})))

(defn textfield-input-block [c props state]
  (dom/div {:className (input-block-css props state)
            :key "inputBlock"
            :ref #(o/set c input-div %)}
           (textfield-input c props)
           (when-not (:full-width props)
             (text-field-label c props state))
           (when (:textbox props)
             (dom/div {:className "mdc-textfield__bottom-line"
                       :key "bottomLine"}))))

(w/def-component TextField
  (componentWillMount [this]
    (adapters/attach-foundation! this textfield-f
                                 (new foundation-c (make-text-field-adapter this))))

  (componentDidMount [this]
    (adapters/init-foundation! this textfield-f))

  (componentWillUnmount [this]
    (adapters/destroy-foundation! this textfield-f)
    (adapters/cleanup-vnode! this cleanups))

  (render [this]
    (let [props (ensure-textfield-props (w/props this))
          input-props (u/remove-props props text-field-props)
          state (w/get-state this)]
      (dom/div nil
        (textfield-input-block this props state)
        (when-not (:full-width props)
          (text-field-help-text this props state))))))



(s/def ::name string?)
(s/def ::label string?)
(s/def ::help-text string?)
(s/def ::multiline boolean?)
(s/def ::help-text-persistent boolean?)
(s/def ::textbox boolean?)
(s/def ::full-width boolean?)


(s/def ::textfield-props
  (s/keys :opt-un [::multiline ::help-text-persistent ::textbox ::full-width]))

(w/def-constructor mdc-text-field TextField
  :opts {:key-fn :id}
  :spec ::textfield-props)