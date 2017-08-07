(ns material-cljs.react.toolbar
  (:require
    [goog.object :as o]
    [material-cljs.utils :as u]
    [material-cljs.react-wrapper.core :as w]
    [material-cljs.dom-helpers :as dom]
    [material-cljs.react.impl.adapters :as adapters]
    ["@material/toolbar/foundation" :as toolbar-f]))


;; TODO: Spec components

;; TODO: implement the rest of the options possible on toolbars
;; TODO: implement toolbar icons
;; TODO: toolbar fixed
;; TODO: toolbar fixed last row
;; TODO: toolbar fixed adjust element
;; TODO: toolbar waterfall
;; TODO: toolbar flexible

(def foundation-c (o/get toolbar-f "default"))
(def foundation "toolbarFoundation_")


(def root      "rootRef")
(def first-row "firstRow")
(def title     "title")

(def first-row-sel   (.. foundation-c -strings -FIRST_ROW_SELECTOR))
(def title-sel       (.. foundation-c -strings -TITLE_SELECTOR))
(def fixed-ajust-sel ".mdc-toolbar-fixed-adjust")

(def change-event (.. foundation-c -strings -CHANGE_EVENT))

(def dom-nodes-mappings
  {first-row first-row-sel
   title title-sel})


(defn make-toolbar-adapter [toolbar]
  (adapters/make-adapter
    adapters/css-classes-mapping
    (adapters/classes-management toolbar :root-classes)

    adapters/scroll-management-mapping
    adapters/scroll-management

    adapters/resize-mapping
    (adapters/event-management js/window "resize")

    {:get "getViewportWidth"}
    (adapters/get-dom-properties-management js/window "innerWidth")

    {:get "getViewportScrollY"}
    (adapters/get-dom-properties-management js/window "pageYOffset")

    {:get "getOffsetHeight"}
    (adapters/get-dom-properties-management  toolbar root "offsetHeight")

    {:get "getFirstRowElementOffsetHeight"}
    (adapters/get-dom-properties-management toolbar first-row "offsetHeight")

    {:emit "notifyChange"}
    (adapters/dispatch-event-management toolbar root change-event)

    {:set-style "setStyle"}
    (adapters/set-style-management toolbar root)

    {:set-style "setStyleForTitleElement"}
    (adapters/set-style-management  toolbar title)

    {:set-style "setStyleForFlexibleRowElement"}
    (adapters/set-style-management  toolbar first-row)

    {:set-style "setStyleForFixedAdjustElement"}
    (adapters/maybe-set-style-management toolbar root fixed-ajust-sel)))

(w/def-component ToolBar
  (componentWillMount [this]
    (adapters/attach-foundation! this foundation
                                 (new foundation-c (make-toolbar-adapter this))))

  (componentDidMount [this]
    (adapters/recover-dom-nodes! this root dom-nodes-mappings)
    (adapters/init-foundation! this foundation))

  (componentWillUnmount [this]
    (adapters/destroy-foundation! this foundation)
    (adapters/cleanup-vnode! this (keys dom-nodes-mappings)))

  (render [this]
    (let [{:keys [className] :as state} (w/get-state this)]
      (u/render-container this
                          dom/header
                          {:className (u/mdc-classes "mdc-toolbar" (:root-classes state #{}))
                           :ref (fn [dom-node]
                                  (o/set this root dom-node))}))))

(def mdc-toolbar (w/factory ToolBar))


(w/def-component ToolBarRow
  (render [this]
    (u/render-container this dom/header {:className "mdc-toolbar__row"})))


(def mdc-toolbar-row (w/factory ToolBarRow))

(defn section-align-class [k]
  (case k
    :start "mdc-toolbar__section--align-start"
    :end "mdc-toolbar__section--align-end"
    ""))

(defn toolbar-section-classes [props]
  (u/mdc-classes
    "mdc-toolbar__section"
    (u/mdc-classes-from-props props
      {:align #(section-align-class %)
       :shrink (constantly "mdc-toolbar__section--shrink-to-fit")})))


(w/def-component ToolBarSection
  (render [this]
    (let [props (w/props this)]
      (u/render-container this dom/header {:className (toolbar-section-classes props)}))))

(def mdc-toolbar-section (w/factory ToolBarSection))



(def default-toolbar-title-props
  {:element dom/span})

(w/def-component ToolBarTitle
  (render [this]
    (let [props (u/ensure-props (w/props this) default-toolbar-title-props)]
      (u/render-container this (:element props) {:className "mdc-toolbar__title"
                                                 ::u/props-filter [:element]}))))

(def mdc-toolbar-title (w/factory ToolBarTitle))


