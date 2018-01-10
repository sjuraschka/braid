(ns braid.client.ui.styles.thread
  (:require
    [garden.arithmetic :as m]
    [garden.units :refer [px em rem]]
    [braid.client.ui.styles.mixins :as mixins]
    [braid.client.ui.styles.vars :as vars]
    [braid.client.ui.styles.misc :refer [drag-and-drop]]))

(defn head [pad]
  [:>.head
   {:min-height "3.5em"
    :position "relative"
    :width "100%"
    :flex-shrink 0
    :padding [[pad (m/* 2 pad) pad pad]]
    :box-sizing "border-box"}

   [:>.tags

    [:>.add
     {:position "relative"}

     [:>span.pill
      mixins/pill-button
      {:letter-spacing "normal !important"}]

     [:>.tag-list
      {:position "absolute"
       :left "100%"
       :margin-left (em 0.5)
       :top "-0.5em"
       :background "white"
       :z-index 100
       :max-height (em 12)
       :overflow-x "auto"}
      (mixins/box-shadow)

      [:>.tag-option
       {:cursor "pointer"
        :white-space "nowrap"
        :padding (em 0.25)}

       [:&:hover
        {:background "#eee"}]

       [:>.rect
        {:width (em 1)
         :height (em 2)
         :display "inline-block"
         :vertical-align "middle"
         :border-radius (px 3)}]

       [:>span
        {:margin (rem 0.25)
         :display "inline-block"
         :vertical-align "middle"}]]]]

    [:>.user :>.tag :>.add
     {:display "inline-block"
      :vertical-align "middle"
      :margin-bottom (rem 0.25)
      :margin-right (rem 0.25)}]]

   [:>.permalink

    [:>button
     mixins/pill-button]]

   [:>.controls
    {:position "absolute"
     :padding pad
     :top 0
     :right 0
     :z-index 10
     :color "#CCC"
     :text-align "right"}

    [:>.control
     {:cursor "pointer"}

     [:&:hover
      {:color "#333"}]

     [:&.close
      [:&::after
       (mixins/fontawesome \uf00d)]]

     [:&.unread
      [:&::after
       (mixins/fontawesome \uf0e2)]]

     [:&.permalink
      [:&::after (mixins/fontawesome \uf0c1)]]

     [:&.mute
      [:&::after (mixins/fontawesome \uf1f6)]]

     [:&.hidden
      {:margin-top (m/* pad 0.5)
       :display "none"}

      [:&::after
       {:font-size "0.9em"
        :margin-right "-0.15em"}]]]

    [:&:hover
     [:>.hidden
      {:display "block"}]]]])

(defn messages [pad]
  [:>.messages
   {:position "relative"
    :overflow-y "auto"
    :padding [[0 pad]]}])

(defn thread [pad]
  [:>.thread
   mixins/flex
   {:margin-right pad
    :min-width vars/card-width
    :width vars/card-width
    :box-sizing "border-box"
    :outline "none"
    :flex-direction "column"
    :height "100%"
    :z-index 101}

   [:&.new
    {:z-index 99}]

   ; switch to ::after to align at top
   [:&::before
    {:content "\"\""
     :flex-grow 1}]

   ;; XXX: does this class actually apply to anything?
   [:&.archived :&.limbo :&.private
    [:>.head::before
     {:content "\"\""
      :display "block"
      :width "100%"
      :height (px 5)
      :position "absolute"
      :top 0
      :left 0
      :border-radius [[vars/border-radius
                       vars/border-radius 0 0]]}]

    [:&.archived
     [:.head::before
      {:background vars/archived-thread-accent-color}]]

    [:&.private
     [:.head::before
      {:background vars/private-thread-accent-color}]]

    [:&.limbo
     [:.head::before
      {:background vars/limbo-thread-accent-color}]]]

   [:&.focused
    [:>.card
     {:box-shadow [[0 (px 10) (px 10) (px 10) "#ccc"]]}]]

   [:>.card
    mixins/flex
    {:flex-direction "column"
     :box-shadow [[0 (px 1) (px 2) 0 "#ccc"]]
     :transition [["box-shadow" "0.2s"]]
     :max-height "100%"
     :background "white"
     :border-radius [[vars/border-radius
                      vars/border-radius 0 0]]}
    (drag-and-drop pad)

    (head pad)
    (messages pad)]])

(defn notice [pad]
  [:>.thread
   [:>.notice
    {:box-shadow [[0 (px 1) (px 2) 0 "#ccc"]]
     :padding pad
     :margin-bottom pad}

    [:&::before
     {:float "left"
      :font-size vars/avatar-size
      :margin-right (rem 0.5)
      :content "\"\""}]]

   [:&.private :&.limbo
    [:>.card
     {; needs to be a better way
      ; which is based on the height of the notice
      :max-height "85%"}]]

   [:&.private
    [:>.notice
     {:background "#D2E7FF"
      :color vars/private-thread-accent-color}

     [:&::before
      (mixins/fontawesome \uf21b)]]]

   [:&.limbo
    [:>.notice
     {:background "#ffe4e4"
      :color vars/limbo-thread-accent-color}

     [:&::before
      (mixins/fontawesome \uf071)]]]])


(defn new-message [pad]
  [:>.message.new
   {:flex-shrink 0
    :padding pad
    :margin 0
    :display "flex"
    :align-items "stretch"}

   [:>.plus
    {:border-radius vars/border-radius
     :width vars/avatar-size
     :cursor "pointer"
     :color "#e6e6e6"
     :box-shadow "0 0 1px 1px #e6e6e6"
     :margin-right "1px"
     :display "flex"
     :align-items "center"
     :justify-content "center"}

    [:&::after
     (mixins/fontawesome \uf067)]

    [:&:hover
     {:color "#ccc"
      :box-shadow "0 0 1px 1px #ccc"}]

    [:&:active
     {:color "#999"
      :box-shadow "0 0 1px 1px #999"}]

    [:&.uploading::after
     (mixins/fontawesome \uf110)
     mixins/spin]]

   [:>.autocomplete-wrapper
    {:flex-grow 2}

    [:>textarea
     {:width "100%"
      :resize "none"
      :border "none"
      :box-sizing "border-box"
      :min-height (em 3.5)
      :padding-left (rem 1)}

     [:&:focus
      {:outline "none"}]]

    [:>.autocomplete
     {:z-index 1000
      :box-shadow [[0 (px 1) (px 4) 0 "#ccc"]]
      :background "white"
      :max-height (em 20)
      :overflow "auto"
      :width vars/card-width
      ; will be an issue when text area expands:
      :position "absolute"
      :bottom (m/* pad 3)}

     [:>.result
      {:padding "0.25em 0.5em"
       :clear "both"}

      [:>.match

       [:>.emojione
        :>.avatar
        :>.color-block
        {:display "block"
         :width "2em"
         :height "2em"
         :float "left"
         :margin "0.25em 0.5em 0.25em 0"}]

       [:>.color-block
        {:width "1em"}]

       [:>.name
        {:height "1em"
         :white-space "nowrap"}]

       [:>.extra
        {:color "#ccc"
         :overflow-y "hidden"
         :max-height "2.5em"}]]

      [:&:hover
       {:background "#eee"}]

      [:&.highlight
       [:.name
        {:font-weight "bold"}]]]]]])
