(ns braid.client.mobile.style
  (:require
    [braid.client.mobile.auth-flow.styles]
    [braid.client.mobile.styles.drawer]
    [braid.client.ui.styles.body]
    [braid.client.ui.styles.embed]
    [braid.client.ui.styles.header]
    [braid.client.ui.styles.imports]
    [braid.client.ui.styles.message]
    [braid.client.ui.styles.mixins :as mixins]
    [braid.client.ui.styles.pills]
    [braid.client.ui.styles.thread]
    [braid.client.ui.styles.vars :as vars]
    [garden.arithmetic :as m]
    [garden.core :refer [css]]
    [garden.stylesheet :refer [at-import]]
    [garden.units :refer [px rem vw vh em]]))

(def styles
  (let [pad (rem 1) ; (vw 5)
        pads "1rem"] ; "5vw"

    (css {:auto-prefix #{:transition
                         :flex-direction
                         :flex-shrink
                         :align-items
                         :animation
                         :flex-grow}
          :vendors ["webkit"]}

         braid.client.ui.styles.imports/imports

         [:body
          {:touch-action "none"
           :height "100vh"}

          [:>#app

           [:>.app
            braid.client.mobile.auth-flow.styles/auth-flow

            [:>.main
             (braid.client.mobile.styles.drawer/drawer pad)

             [:>.page
              {:position "fixed"
               :top 0
               :left 0
               :right 0
               :bottom 0
               :z-index 50
               :background "#CCC"}

              (braid.client.ui.styles.header/group-header "2.5rem")

              [:>.global-settings
               {:overflow-y "auto"
                :height "90%"
                :margin-left "5em"}]

              [:>.group-header
               [:>.bar
                mixins/flex
                {:justify-content "space-between"
                 :border-radius 0
                 :box-shadow "none"}

                [:>.group-name
                 {:padding 0}]

                [:>.badge-wrapper
                 {:min-width (em 1.5)}
                 [:>.badge
                  mixins/pill-box
                  {:background-color "#b53737 !important"
                   :margin-left (em 0.5)
                   :margin-top (em 0.5)}]]

                [:>.spacer
                 {:flex-grow 2}]]]

              [:>.threads
               {:height "100%"
                :box-sizing "border-box"}

               [:>.container

                [:>.panels

                 [:>.panel
                  {:position "relative"}

                  [:>.arrow-prev
                   :>.arrow-next
                   {:position "absolute"
                    :bottom "50%"
                    :width "10px"
                    :height "40px"}]

                  [:>.arrow-prev
                   {:left 0}]

                  [:>.arrow-next
                   {:right 0}]

                  [:>.thread
                   mixins/flex
                   {:width "100vw"
                    :height "calc(100% - 2.5rem)"
                    :background "#bbb"
                    :flex-direction "column"
                    :justify-content "flex-end"
                    :overflow "none"}

                   [:>.card
                    (braid.client.ui.styles.thread/head 0)
                    mixins/flex
                    {:flex-direction "column"
                     :justify-content "flex-end"
                     :background-color "white"}
                    [:>.head
                     {:flex-shrink 0
                      :min-height (em 3.5)
                      :position "relative"
                      :width "100%"
                      :background-color "#333333"}
                     [:>.notice
                      {:margin [[(px 5) (px 5)]]}
                      [:>.private :>.limbo
                       {:padding [[(px 5) (px 5)]]
                        :border-radius (px 3)}]
                      [:>.private
                       {:background "#D2E7FF"
                        :color vars/private-thread-accent-color}]

                      [:>.limbo
                       {:background "#ffe4e4"
                        :color vars/limbo-thread-accent-color}]]

                     [:>.tags
                      {:padding (em 0.5)}
                      (braid.client.ui.styles.pills/tag)
                      (braid.client.ui.styles.pills/user)
                      [:>.add
                       {:font-size (em 2)}]]

                     [:>.close
                      {:position "absolute"
                       :top 0
                       :right 0
                       :color "#CCC"
                       :padding-right (px 5)}

                      [:&::after
                       {:font-size (em 2)}
                       (mixins/fontawesome \uf00d)]]]

                    [:>.message.new

                     [:>button.send
                      {:background  "blue"
                       :border "none"
                       :color "white"}

                      [:&::before
                       {:font-size (em 2)}
                       (mixins/fontawesome \uf1d8)]

                      [:&:disabled
                       {:background "#ccc"
                        :color "#aaa"}]]]

                    (braid.client.ui.styles.thread/messages pad)

                    [:>.messages
                     {:flex-grow 1
                      :padding-top (em 0.5)
                      :-webkit-overflow-scrolling "touch"}

                     braid.client.ui.styles.message/message
                     [:>.message
                      (braid.client.ui.styles.embed/embed pad)]]


                    (braid.client.ui.styles.thread/new-message pad)]]]]]]]]]]]

         braid.client.ui.styles.body/body)))
