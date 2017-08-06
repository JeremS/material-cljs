(ns material-cljs.react-wrapper.core
  #?(:clj (:require
            [clojure.spec.alpha :as s]))
  #?(:cljs (:require
             [clojure.spec.alpha :as s]
             [goog.object :as o]
             ["react/react" :refer [createElement]]
             ["create-react-class" :as cc]))
  #?(:cljs (:require-macros
             [material-cljs.react-wrapper.core])))

#?(:cljs
   (do
     (def create-class cc)

     (def mc-wrapped "mc$wrapped")

     (defn wrap [v]
       (js-obj mc-wrapped v))

     (defn un-wrap [v]
       (o/get v mc-wrapped))


     (defn react-props [c]
       (.-props c))

     (defn react-state [c]
       (.-state c))

     (def props-from-react-props un-wrap)
     (def state-from-react-state un-wrap)

     (defn children-from-react-props [props]
       (.-children props))

     (defn props [c]
       (props-from-react-props (react-props c)))

     (defn children [c]
       (children-from-react-props (react-props c)))

     (defn get-state [c]
       (state-from-react-state (react-state c)))


     (defn wrap-updater [updater]
       (fn [r-state r-props]
         (let [current-state (state-from-react-state r-state)
               current-props (props-from-react-props r-props)
               new-state (updater current-state
                                  current-props)]
           (wrap new-state))))

     (defn set-state! [c updater]
       (.setState c (wrap-updater updater)))

     (defn force-children [x]
       (cond->> x
                (seq? x) (into [] (map force-children))))

     (defn dom-factory [node-name]
       (fn [props & args]
         (createElement node-name (clj->js props) (force-children args))))

     (defn factory
       ([class]
        (factory class {:key-fn :key}))
       ([class opts]
        (if (string? class)
          (dom-factory class)
          (let [key-fn (:key-fn opts (constantly nil))]
            (fn [props & children]
              (let [react-props (wrap props)
                    ref (:ref props nil)]
                (o/set react-props "ref" ref)
                (when-let [key (key-fn props)]
                  (o/set react-props "key" key))
                (createElement
                  class
                  react-props
                  (force-children children))))))))))







(s/def ::def-component-method
  (s/cat :name symbol?
         :arg-list (s/spec (s/cat :this symbol?
                                  :args (s/* symbol?)))
         :body (s/* any?)))

(s/def ::def-component-args
  (s/cat :component-name symbol?
         :component-methods (s/* (s/spec ::def-component-method))))


(def default-should-component-update
  `(shouldComponentUpdate [this# next-props# next-state#]
     (let [props# (props this#)
           new-props# (and next-props# (props-from-react-props next-props#))

           state# (get-state this#)
           new-state# (and next-state# (state-from-react-state next-state#))

           children# (children this#)
           new-children# (and next-props# (children-from-react-props next-props#))]
       (or (not= props# new-props#)
           (not= state# new-state#)
           (not= children# new-children#)))))


(def default-get-initial-state
  '(getInitialState [this] {}))

(def default-methods [default-should-component-update
                      default-get-initial-state])

(def parsed-default-methods
  (map (partial s/conform ::def-component-method) default-methods))

(defn reshape-getInitialState [conformed-method]
  (let [{:keys [arg-list body]} conformed-method]
    `(fn ~(:args arg-list [])
       (cljs.core/this-as ~(:this arg-list)
         (wrap ~@body)))))


(defn method-template [conformed-method]
  (let [{:keys [arg-list body]} conformed-method]
    `(fn ~(:args arg-list [])
       (cljs.core/this-as ~(:this arg-list)
         ~@body))))


(defn reshape [n m]
  (case n
    :getInitialState (reshape-getInitialState m)
    (method-template m)))

(defn make-method-entry [conformed-method]
  (let [{:keys [name]} conformed-method
        name (keyword name)]
    [name (reshape name conformed-method)]))

(defn make-methods-map [component-name methods]
  (transduce (map make-method-entry)
             (fn
               ([] {})
               ([acc] (assoc acc :displayName component-name))
               ([acc [k v]] (assoc acc k v)))
             methods))


#?(:clj
   (do
     (defmacro def-component [& args]
       (let [{:keys [component-name component-methods]} (s/conform ::def-component-args args)
             display-name (str (get-in &env [:ns :name])
                               "."
                               (name component-name))
             component-methods (concat parsed-default-methods component-methods)]
         `(def ~component-name
            (create-class
              (cljs.core/clj->js ~(make-methods-map display-name  component-methods))))))

     (s/fdef def-component
             :args ::def-component-args
             :ret any?)))


