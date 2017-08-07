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
            [material-cljs.react.elevation :as elevation]
            [material-cljs.react.card :as card]))

;; TODO: find a way to assign react keys to component that don't have one in their props

(enable-console-print!)


(defn toolbar []
  (toolbar/mdc-toolbar {:key "toolbar"}
    (toolbar/mdc-toolbar-row {:key "toolbarRow1"}
      (toolbar/mdc-toolbar-section {:key "toolbarRow1Section1"}
        (toolbar/mdc-toolbar-title {:key "toolbarRow1Section1Title"
                                    :element dom/h1}
                                   "Numbers")))))

(defn textfields []
  [(grid/mdc-grid-inner {:key "textfieldLine1"}
     (grid/mdc-grid-cell {:span 6 :key "1"}
       (tfield/mdc-text-field {:label "text 1"
                               :help-text "help 1"
                               :id 1}))


     (grid/mdc-grid-cell {:span 6 :key "2"}
       (tfield/mdc-text-field {:label "text 2"
                               :id 2
                               :textbox true
                               :help-text "help boxed"})))

   (grid/mdc-grid-inner {:key "textfieldLine2"}
     (grid/mdc-grid-cell {:span 6 :key "1"}
       (tfield/mdc-text-field {:label "text 1"
                               :help-text "help 1"
                               :id 1
                               :disabled true}))


     (grid/mdc-grid-cell {:span 6 :key "2"}
       (tfield/mdc-text-field {:label "text 2"
                               :id 2
                               :full-width true})))

   (grid/mdc-grid-inner {:key "textfieldLine3"}
     (grid/mdc-grid-cell {:span 12 :key "1"}
       (tfield/mdc-text-field {:label "multiline text"
                               :help-text "help 1"
                               :id 1
                               :multiline true
                               :rows 10
                               :full-width true})))])

(defn buttons []
  (grid/mdc-grid-inner {:key "buttonLine"}
    (vec
      (cons (grid/mdc-grid-cell {:span 2 :key "0"}
              (button/mdc-button {:key 0} "normal"))
            (rest
              (map-indexed (fn [i v]
                             (grid/mdc-grid-cell {:span 2 :key i}
                               (button/mdc-button {v true :key i} (name v))))
                           [:enabled :dense :raised :compact :primary :accented]))))))

(defn elevations []
  [(grid/mdc-grid-inner {:key "elevationLine1"}
     (for [i (range 1 7)]
       (grid/mdc-grid-cell {:span 2 :key i}
         (elevation/mdc-elevation {:z i :key i}
                                  (str "elevation " i)))))

   (grid/mdc-grid-inner {:key "elevationLine2"}
     (for [i (range 7 13)]
       (grid/mdc-grid-cell {:span 2 :key i}
         (elevation/mdc-elevation {:z i :key i}
                                  (str "elevation " i)))))

   (grid/mdc-grid-inner {:key "elevationLine3"}
     (for [i (range 13 19)]
       (grid/mdc-grid-cell {:span 2 :key i}
         (elevation/mdc-elevation {:z i :key i}
                                  (str "elevation " i)))))

   (grid/mdc-grid-inner {:key "elevationLine4"}
     (for [i (range 19 25)]
       (grid/mdc-grid-cell {:span 2 :key i}
         (elevation/mdc-elevation {:z i :key i}
                                  (str "elevation " i)))))])


(def lorem-ipsum
  "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod
  tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim
  veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
  commodo consequat.")


(defn cards []
  (grid/mdc-grid-inner {:key "cardsLine"}
    (grid/mdc-grid-cell {:key "1" :span 6 :align :middle}
      (card/mdc-card {:key "card"}
        (card/mdc-card-header {:key "header"}
          (card/mdc-card-title {:key "title" :large-title true} "Vertical card")
          (card/mdc-card-sub-title {:key "subtitle"} "With horizontal actions"))
        (card/mdc-card-text {:key "text"} lorem-ipsum)
        (card/mdc-card-actions {:key "actions"}
          (card/mdc-card-action-button {:key "Action1"} "Action1")
          (card/mdc-card-action-button {:key "Action2"} "Action2"))))


    (grid/mdc-grid-cell {:key "2" :span 6 :align :middle}
      (card/mdc-card {:horizontal true :key "card"}
        (card/mdc-card-header {:key "header"}
          (card/mdc-card-title {:key "title" :large-title true} "Horizontal card")
          (card/mdc-card-sub-title {:key "subtitle"} "With vertical actions"))
        (card/mdc-card-text {:key "text"} lorem-ipsum)
        (card/mdc-card-actions {:key "actions" :vertical true}
          (card/mdc-card-action-button {:key "Action1"} "Action1")
          (card/mdc-card-action-button {:key "Action2"} "Action2"))))))



(defn grid []
  (grid/mdc-grid {:key "grid"}
    (textfields)
    (buttons)
    (elevations)
    (cards)))




(r-dom/render
  (dom/div nil (toolbar) (grid))
  (.getElementById js/document "app"))

