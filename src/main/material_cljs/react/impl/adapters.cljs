(ns material-cljs.react.impl.adapters
  (:require
    [goog.object :as o]
    [material-cljs.utils :as u]
    [material-cljs.react-wrapper.core :as w]
    [material-cljs.dom-helpers :as dom]
    ;; TODO: maybe mode into the toolbar namespace
    ["@material/toolbar/util" :as t-utils]))

;; ---------------------------------------------------------------------------------------------------------------------
;; Adapters functions constructors
(defn classes-management [c state-key]
  {:add (fn [class-name]
          (w/set-state! c
                        (fn [state _]
                          (update state state-key (fnil conj #{}) class-name))))

   :remove (fn [class-name]
             (w/set-state! c (fn [state _]
                               (update state state-key disj class-name))))

   :has? (fn [class-name]
           (-> (w/get-state c)
               (get state-key #{})
               (contains? class-name)))})


(defn attrs-management[c state-key]
  {:set-attr (fn [attr-name attr-value]
               (w/set-state! c (fn [state _]
                                 (assoc-in state [state-key attr-name] attr-value))))

   :remove-attr (fn [attr-name]
                  (w/set-state! c (fn [state _]
                                    (update state state-key dissoc attr-name))))})

(defn event-management
  ([dom-node event-name]
   {:register (fn [h] (.addEventListener dom-node event-name h))
    :deregister (fn [h] (.removeEventListener dom-node event-name h))})
  ([v-node ref event-name]
   {:register (fn [h] (.addEventListener (o/get v-node ref) event-name h))
    :deregister (fn [h] (.removeEventListener (o/get v-node ref) event-name h))}))



(defn interaction-handler-mananagement [v-node root-ref]
  {:register (fn [event-name h]
               (.addEventListener (o/get v-node root-ref) event-name h))
   :deregister (fn [event-name h]
                 (.removeEventListener (o/get v-node root-ref) event-name h))})


;; TODO: use a js/Proxy here
(defn native-input-management [v-node input-ref]
  {:get-native-input (fn [] (o/get v-node input-ref))})


;; TODO: maybe move into the toolbar namespace
(def scroll-management
  {:register (fn [h] (.addEventListener js/window "scroll" h (t-utils/applyPassive)))
   :deregister (fn [h] (.removeEventListener js/window "scroll" h (t-utils/applyPassive)))})


(defn get-dom-properties-management
  ([dom-node p-name]
   {:get (fn [] (o/get dom-node p-name))})
  ([v-node ref p-name]
   {:get (fn [] (-> v-node (o/get ref) (o/get p-name)))}))


(defn set-style-management [v-node attr]
  {:set-style (fn [property value]
                (.. (o/get v-node attr) -style (setProperty property value)))})

(defn maybe-set-style-management [v-node root-node-name sel]
  {:set-style (fn [property value]
                (let [dom-root (o/get v-node root-node-name)]
                  (when-let [maybe-node (dom/query-selector dom-root sel)]
                    (.. maybe-node -style (setProperty property value)))))})

;; inspired from the default implementation
;; in @material/base/component.js
(defn make-event
  ([event-type event-data]
   (make-event event-type event-data false))
  ([event-type event-data bubble?]
   (if (exists? js/CustomEvent)
     (js/CustomEvent. event-type
                      #js {:detail event-data
                           :bubbles bubble?})
     (doto (.createEvent js/document "CustomEvent")
       (.initCustomEvent event-type bubble? false event-data)))))


(defn dispatch-event-management [v-node ref event-type]
  {:emit (fn [event-data]
           (let [dom-node-source (o/get v-node ref)]
             (.dispatchEvent dom-node-source (make-event event-type event-data))))})




(def focusable-elements
  "a[href],
  area[href],
  input:not([disabled]),
  select:not([disabled]),
  textarea:not([disabled]),
  button:not([disabled]),
  iframe,
  object,
  embed,
  [tabindex],
  [contenteditable]")


;; ---------------------------------------------------------------------------------------------------------------------
;; Common adapter name mappings
(def css-classes-mapping
  {:add    "addClass"
   :remove "removeClass"
   :has?   "hasClass"})

(def scroll-management-mapping
  {:register "registerScrollHandler"
   :deregister "deregisterScrollHandler"})


(def resize-mapping
  {:register "registerResizeHandler"
   :deregister "deregisterResizeHandler"})

;; ---------------------------------------------------------------------------------------------------------------------
;; Adapters construction helper
(defn add-handlers
  ([adapter] adapter)
  ([adapter mapping handlers]
   (reduce (fn [adapter k]
             (doto adapter
               (o/set (mapping k) (handlers k))))
           adapter
           (keys mapping)))
  ([adapter mapping handlers & args]
   (reduce (fn [adapter [m h]]
             (add-handlers adapter m h))
           (add-handlers adapter mapping handlers)
           (partition 2 args))))

(defn make-adapter [& mapping-handlers]
  (apply add-handlers (js-obj) mapping-handlers))

;; ---------------------------------------------------------------------------------------------------------------------
;; React components lifecycle helpers

(defn recover-dom-nodes! [parent-vnode root-node-attr mappings]
  (let [root-node (o/get parent-vnode root-node-attr)]
    (reduce-kv (fn [_ attr sel]
                 (o/set parent-vnode attr (dom/query-selector root-node sel)))
               nil
               mappings)))

(defn cleanup-vnode! [parent-vnode attrs]
  (reduce (fn [_ attr]
            (o/set parent-vnode attr nil))
          nil
          attrs))

(defn attach-foundation! [parent-vnode attr foundation]
  (o/set parent-vnode attr foundation))

(defn init-foundation! [parent-vnode attr]
  (.init (o/get parent-vnode attr)))

(defn destroy-foundation! [parent-vnode attr]
  (let [foundation (o/get parent-vnode attr)]
    (.destroy foundation)))
