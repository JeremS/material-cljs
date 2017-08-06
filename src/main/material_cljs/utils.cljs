(ns material-cljs.utils
  (:require
    [goog.object :as o]
    [clojure.string :as string]
    [clojure.set :refer [union]]
    [material-cljs.react-wrapper.core :as w]))



;; Helpers for mdc components
(defn split-classes [s]
  (string/split s #"\s+"))

(defn join-classes [ss]
  (string/join \space ss))

(defn make-mdc-class [prefix & args]
  (apply str prefix args))

(defn mdc-classes-from-props [props dispatch]
  (persistent!
    (reduce-kv (fn [acc prop-name prop->mdc-class]
                 (if-let [prop-val (get props prop-name nil)]
                   (conj! acc (prop->mdc-class prop-val))
                   acc))
               (transient #{})
               dispatch)))

(defn mdc-classes
  ([base-mdc-class & classes-sets]
   (join-classes (apply union #{base-mdc-class} classes-sets))))


(defn merge-classes [mdc-classes given-classes]
  (transduce (mapcat #(split-classes %))
             (fn
               ([] (transient #{}))
               ([acc] (-> acc persistent! join-classes))
               ([acc v] (conj! acc v)))
             [mdc-classes given-classes]))


(defn specific-props [props ks]
  (select-keys props ks))

(defn remove-props [props ks]
  (when props
    (persistent!
      (reduce #(dissoc! %1 %2)
              (transient props)
              ks))))

(defn ensure-props [props defaultprops]
  (persistent!
    (reduce (fn [props k]
              (cond-> props
                      (not (get props k)) (assoc! k (defaultprops k))))
            (transient props)
            (keys defaultprops))))

(defn render-container [component ctor default-props]
  (let [given-props (w/props component)
        new-css-classes (merge-classes (:className default-props) (:className given-props))
        props-filter (::props-filter default-props)]

    (ctor (-> default-props
              (merge given-props)
              (assoc :className new-css-classes)
              (cond-> props-filter
                      (remove-props (conj  props-filter ::props-filter))))
          (w/children component))))

