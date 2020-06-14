(ns webtest.core
  (:require [org.httpkit.server :as s]
            [compojure.core :refer [routes defroutes POST GET ANY]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [clojure.pprint :as pp]
            [clojure.data.json :as json]))

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

(defn echo-request [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (->>
           (pp/pprint req)
           (str "Request Object: " req))})

(defroutes app-routes
  (GET "/" [:as req]
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body "{\"hello\": \"world\"}"})
  (GET "/echo" [] echo-request)
  (GET "/:username" [username :as req]
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (format "{\"hello\": \"%s\"}" username)}))

(defn create-server []
  (s/run-server (app) {:port 8080}))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn -main [& args]
  ;(reset! server (s/run-server (app) {:port 8080})))
  (reset! server (s/run-server (wrap-defaults #'app-routes api-defaults) {:port 8080})))
