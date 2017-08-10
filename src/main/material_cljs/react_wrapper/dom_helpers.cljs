(ns material-cljs.dom-helpers
  (:require
    [material-cljs.react-wrapper.core :refer [factory]]))

;; dom node constructors
(def h1 (factory "h1"))
(def h2 (factory "h2"))
(def span (factory "span"))
(def p (factory "p"))
(def div (factory "div"))
(def section (factory "section"))
(def header (factory "header"))
(def a (factory "a"))


(def button (factory "button"))
(def input (factory "input"))
(def label (factory "label"))
(def textarea (factory "textarea"))

(def style (factory "style"))


;; dom events
(def focus-ev "focus")
(def blur-ev "blur")
(def input-ev "input")
(def keydown-ev "keydown")

(defn query-selector [dom-node sel]
  (.querySelector dom-node sel))

