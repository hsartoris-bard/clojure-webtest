(ns webtest.core
  (:require [org.httpkit.server :as s]
            [compojure.core :refer [routes POST GET ANY]]))

(defonce server (atom nil))

(defn handler [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body "{\"hello\": \"world\"}"})

(defn app []
  (routes
    (GET "/" [:as req]
         {:status 200
          :headers {"Content-Type" "application/json"}
          :body "{\"hello\": \"world\"}"})
    (GET "/:username" [username :as req]
         {:status 200
          :headers {"Content-Type" "application/json"}
          :body (format "{\"hello\": \"%s\"}" username)})))

(defn create-server []
  (s/run-server (app) {:port 8080}))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn -main [& args]
  (reset! server (s/run-server (app) {:port 8080})))
