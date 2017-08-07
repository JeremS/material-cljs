(ns material-cljs.react-wrapper.utils)

(def ^:dynamic *print-info* false)

(defn make-print-wrapper [message]
  (fn [f]
    (fn [& args]
      (println message)
      (binding [*print-info* true]
        (let [result (apply f args)]
          result)))))
