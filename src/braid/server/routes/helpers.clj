(ns braid.server.routes.helpers
  (:require
    [ring.middleware.anti-forgery :as anti-forgery]
    [braid.server.db.user :as user]))

(defn logged-in? [req]
  (when-let [user-id (get-in req [:session :user-id])]
    (user/user-id-exists? user-id)))

(defn current-user [req]
  (when-let [user-id (get-in req [:session :user-id])]
    (when (user/user-id-exists? user-id)
      (user/user-by-id user-id))))

(defn current-user-id [req]
  (when-let [user-id (get-in req [:session :user-id])]
    (when (user/user-id-exists? user-id)
      user-id)))

(defn session-token []
  anti-forgery/*anti-forgery-token*)

(defn error-response [status msg]
  {:status status
   :headers {"Content-Type" "application/edn; charset=utf-8"}
   :body (pr-str {:error msg})})

(defn edn-response [clj-body]
  {:headers {"Content-Type" "application/edn; charset=utf-8"}
   :body (pr-str clj-body)})
