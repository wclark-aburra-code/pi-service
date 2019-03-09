(ns dev
  (:require [io.pedestal.service.http :as http]
            [server-sent-events.service :as service]
            [server-sent-events.server :as server]))

(def service (-> service/service
                 (merge  {:env :dev
                          ::http/join? false
                          ::http/routes #(deref #'service/routes)})
                 (http/default-interceptors)
                 (http/dev-interceptors)))

(defn start
  [& [opts]]
  (server/create-server (merge service opts))
  (http/start server/service-instance))

(defn stop
  []
  (http/stop server/service-instance))

(defn restart
  []
  (stop)
  (start))
