(ns material-cljs.core
  (:require ["react/react" :as react]
            ["react-dom" :as r-dom]
            ["create-react-class" :as cc]
            [material-cljs.utils :as u]
            [material-cljs.react-wrapper.core :as w]
            [material-cljs.dom-helpers :as dom]
            [material-cljs.react.toolbar :as toolbar]
            [material-cljs.react.grid :as grid]
            [material-cljs.react.textfield :as tfield]
            [material-cljs.react.button :as button]
            [material-cljs.react.elevation :as elevation]))



(enable-console-print!)


(defn toolbar []
  (toolbar/mdc-toolbar {:key "toolbar"}
    (toolbar/mdc-toolbar-row {:key "toolbarRow1"}
      (toolbar/mdc-toolbar-section {:key "toolbarRow1Section1"}
        (toolbar/mdc-toolbar-title {:key "toolbarRow1Section1Title"
                                    :element dom/h1}
                                   "Numbers")))))

(defn grid []
  (grid/mdc-grid {:key "grid"}
    (grid/mdc-grid-inner {:key "inner1"}
      (grid/mdc-grid-cell {:span 4 :key "1"}
        (grid/mdc-grid-inner {:key "inner1innner1"}
          (grid/mdc-grid-cell {:span 4 :key "1.1"} "1.1")
          (grid/mdc-grid-cell {:span 4 :key "1.2"} "1.2")
          (grid/mdc-grid-cell {:span 4 :key "1.3"} "1.3")))
      (grid/mdc-grid-cell {:span 4 :key "2"} "2")
      (grid/mdc-grid-cell {:span 4 :key "3"} "3"))
    (grid/mdc-grid-inner {:key "inner2"}
      (grid/mdc-grid-cell {:span 6 :key "1"}
        (tfield/mdc-text-field {:label "text 1"
                                :help-text "help 1"
                                :id 1}))


      (grid/mdc-grid-cell {:span 6 :key "2"}
        (tfield/mdc-text-field {:label "text 2"
                                :id 2
                                :textbox true
                                :help-text "help boxed"})))

    (grid/mdc-grid-inner {:key "inner3"}
      (grid/mdc-grid-cell {:span 6 :key "1"}
        (tfield/mdc-text-field {:label "text 1"
                                :help-text "help 1"
                                :id 1
                                :disabled true}))


      (grid/mdc-grid-cell {:span 6 :key "2"}
        (tfield/mdc-text-field {:label "text 2"
                                :id 2
                                :full-width true})))

    (grid/mdc-grid-inner {:key "inner4"}
      (grid/mdc-grid-cell {:span 12 :key "1"}
        (tfield/mdc-text-field {:label "multiline text"
                                :help-text "help 1"
                                :id 1
                                :multiline true
                                :rows 10
                                :full-width true})))
    (grid/mdc-grid-inner {:key "button-line"}
      (cons (grid/mdc-grid-cell {:span 2 :key "0"}
              (button/mdc-button nil "normal"))
            (rest
              (map-indexed (fn [i v]
                             (grid/mdc-grid-cell {:span 2 :key i}
                               (button/mdc-button {v true} (name v))))
                           [:enabled :dense :raised :compact :primary :accented]))))

    (grid/mdc-grid-inner {:key "elevationLine1"}
      (for [i (range 1 7)]
        (grid/mdc-grid-cell {:span 2 :key i}
          (elevation/mdc-elevation {:z i}
            (str "elevation " i)))))

    (grid/mdc-grid-inner {:key "elevationLine2"}
      (for [i (range 7 13)]
        (grid/mdc-grid-cell {:span 2 :key i}
          (elevation/mdc-elevation {:z i}
            (str "elevation " i)))))

    (grid/mdc-grid-inner {:key "elevationLine3"}
      (for [i (range 13 19)]
        (grid/mdc-grid-cell {:span 2 :key i}
          (elevation/mdc-elevation {:z i}
            (str "elevation " i)))))

    (grid/mdc-grid-inner {:key "elevationLine4"}
      (for [i (range 19 25)]
        (grid/mdc-grid-cell {:span 2 :key i}
          (elevation/mdc-elevation {:z i}
            (str "elevation " i)))))))


(r-dom/render
  (dom/div nil (toolbar) (grid))
  (.getElementById js/document "app"))

