(ns material-cljs.react.toolbar
  (:require
    [goog.object :as o]
    [clojure.spec.alpha :as s]
    [material-cljs.utils :as u]
    [material-cljs.react-wrapper.core :as w]
    [material-cljs.dom-helpers :as dom]
    [material-cljs.react.impl.adapters :as adapters]
    ["@material/toolbar/foundation" :as toolbar-f]))


;; TODO: toolbar flexible

;; TODO: Waterfall doesn't work properly?
;; TODO: Investigate the scroll events handling there seems to be an issue where the event handlers aren't properly deregistered.
(def foundation-c (o/get toolbar-f "default"))
(def foundation "toolbarFoundation_")


(def root      "rootRef")
(def first-row "firstRow")
(def title     "title")
(def fixed-adjuster "fixedAdjuster_")

(def first-row-sel   (.. foundation-c -strings -FIRST_ROW_SELECTOR))
(def title-sel       (.. foundation-c -strings -TITLE_SELECTOR))

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
    (adapters/maybe-set-style-management toolbar fixed-adjuster)))

(def toolbar-props->mdc-classes
  {:fixed (constantly "mdc-toolbar--fixed")
   :fixed-last-row (constantly "mdc-toolbar--fixed-lastrow-only")
   :waterfall (constantly "mdc-toolbar--waterfall")})


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
    (let [props (w/props this)
          {:keys [className] :as state} (w/get-state this)]
      (dom/div nil
        (u/render-container this
                            dom/header
                            {:className (u/mdc-classes "mdc-toolbar"
                                          (:root-classes state #{})
                                          (u/mdc-classes-from-props props toolbar-props->mdc-classes))
                             ::u/props-filter [:fixed :fixed-last-row :waterfall]
                             :key "actualToobar"
                             :ref (fn [dom-node]
                                    (o/set this root dom-node))})
        (when (:fixed props)
          (dom/div {:className "mdc-toolbar-fixed-adjust"
                    :key "fixedAdjuster"
                    :ref (fn [dom-node]
                           (o/set this fixed-adjuster dom-node))}))))))

(s/def ::fixed boolean?)
(s/def ::fixed-last-row boolean?)
(s/def ::waterfall boolean?)
(s/def ::toolbar-props (s/keys :opt-un [::fixed ::fixed-last-row ::waterfall]))

(w/def-constructor mdc-toolbar ToolBar :spec ::toolbar-props)

;; ---------------------------------------------------------------------------------------------------------------------
(w/def-component ToolBarRow
  (render [this]
    (u/render-container this dom/header {:className "mdc-toolbar__row"})))


(w/def-constructor mdc-toolbar-row ToolBarRow)


;; ---------------------------------------------------------------------------------------------------------------------
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
      (u/render-container this dom/header {:className (toolbar-section-classes props)
                                           ::u/props-filter [:align :shrink]}))))

(s/def ::align #{:start :end})
(s/def ::shrink boolean?)
(s/def ::toolbar-section-props (s/keys :opt-un [::align ::shrink]))

(w/def-constructor mdc-toolbar-section ToolBarSection :spec ::toolbar-section-props)


;; ---------------------------------------------------------------------------------------------------------------------
(def default-toolbar-title-props
  {:element dom/span})

(w/def-component ToolBarTitle
  (render [this]
    (let [props (u/ensure-props (w/props this) default-toolbar-title-props)]
      (u/render-container this (:element props) {:className "mdc-toolbar__title"
                                                 ::u/props-filter [:element]}))))

(s/def ::element fn?)
(s/def ::toolbar-title-props (s/keys :opt-un [::element]))

(w/def-constructor mdc-toolbar-title ToolBarTitle :spec ::toolbar-title-props)


;; ---------------------------------------------------------------------------------------------------------------------

(s/def ::icon-ctor fn?)
(s/def ::menu boolean?)
(s/def ::toolbar-icons-props (s/keys :req-un [::icon-ctor]
                                     :opt-un [::menu]))

(def default-icon-props
  {:size 24})

(w/def-component ToolbarIcon
  (render [this]
          (let [props (u/ensure-props (w/props this) default-icon-props)
                mdc-class (if (:menu props)
                            "mdc-toolbar__icon--menu"
                            "mdc-toolbar__icon")]
            (u/render-container this (:icon-ctor props) {:className mdc-class
                                                         :size (:size props)
                                                         ::u/props-filter [:icon-ctor :menu]}))))


(w/def-constructor mdc-toolbar-icon ToolbarIcon :spec ::toolbar-icons-props)

