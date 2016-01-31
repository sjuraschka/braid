(ns chat.client.views.header
  (:require [om.core :as om]
            [om.dom :as dom]
            [chat.client.store :as store]
            [chat.client.views.search-bar :refer [search-bar-view]]
            [chat.client.views.pills :refer [tag-view user-view]]))

(defn header-view [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:className "header"}
        (let [users (->> (store/users-in-open-group)
                         (remove (fn [user] (= (get-in @store/app-state [:session :user-id]) (user :id)))))
              users-online (->> users
                                (filter (fn [user] (= :online (user :status)))))]
          (dom/div #js {:className "users"}
            (dom/div #js {:className "title" :title "Users"}
              (count users-online))
            (apply dom/div #js {:className "modal"}
              (dom/h2 nil "Online")
              (->> users-online
                   (map (fn [user]
                          (dom/div nil
                            (om/build user-view user))))))))
        (dom/div #js {:className "tags"}
          (dom/div #js {:className "title"
                        :title "Tags"
                        :onClick (fn [e] (store/set-page! {:type :channels}))})
          (dom/div #js {:className "modal"}
            (apply dom/div nil
              (->> (@store/app-state :tags)
                   vals
                   (filter (fn [t] (= (@store/app-state :open-group-id) (t :group-id))))
                   (filter (fn [t] (store/is-subscribed-to-tag? (t :id))))
                   (sort-by :threads-count)
                   reverse
                   (map (fn [tag]
                          (dom/div nil (om/build tag-view tag))))))))
        (dom/div #js {:className "help"}
          (dom/div #js {:className "title" :title "Help"})
          (dom/div #js {:className "modal"}
            (dom/p nil "Conversations must be tagged to be seen by other people.")
            (dom/p nil "Tag a conversation by mentioning a tag in a message: ex. #general")
            (dom/p nil "You can also mention users to add them to a conversation: ex. @raf")
            (dom/p nil "Add emoji by using :shortcodes: (they autocomplete).")))

        (om/build search-bar-view (data :page))))))