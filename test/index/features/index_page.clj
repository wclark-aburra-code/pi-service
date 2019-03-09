(ns clj-webdriver-tutorial.features.homepage
  (:require [clojure.test :refer :all]
            [ring.adapter.jetty :refer [run-jetty]]
            [clj-webdriver.taxi :refer :all]
            [clj-webdriver-tutorial.features.config :refer :all]
            [clj-webdriver-tutorial.handler :refer [app-routes]]))
            ;; the dependencies in project.clj have to be modified for your local environment in order to run the selenium tests
(defn start-server []
  (loop [server (run-jetty app-routes {:port test-port, :join? false})]
    (if (.isStarted server)
      server
      (recur server))))
(defn stop-server [server]
  (.stop server))

(defn start-browser []
  (set-driver! {:browser :firefox}))

(defn stop-browser []
  (quit))

(deftest homepage-test
  (let [server (start-server)]
    (start-browser)
    (to test-base-url)
    (is (includes? (text "body") "Leibniz"))
    (is (includes? (text "body") "Euler"))
    (stop-browser)
    (stop-server server)))

(defn run-leibniz-and-wait []
  (click "#run-leibniz-btn")
  (Thread/sleep 5000)
)

(defn run-euler-and-wait []
  (click "#run-euler-btn")
  (Thread/sleep 5000)
)

(defn stop-euler []
  (click "#reset-euler-btn")
)

(defn stop-leibniz []
  (click "#reset-leibniz-btn")
)

(deftest run-euler-test
  (let [server (start-server)]
    (start-browser)
    (to test-base-url)
    (is (not (enabled? "#reset-euler-btn")))
    (run-euler-and-wait)
    (is (not (enabled? "#run-leibniz-btn")))
    (is (not (enabled? "#run-euler-btn")))
    (is (includes? (text "#euler-items-list") "2.449489742783178"))
    (is (enabled? "#reset-euler-btn"))
    (is (includes? (text "#euler-items-list") "2.857738033247041"))
    (is (not (includes? (text "#euler-items-list") "3.14159265358979")))
    (stop-euler)
    (is (includes? (text "body") "Scroll through list to see more results"))
    (is (not (enabled? "#reset-euler-btn")))
    (is (enabled? "#run-euler-btn"))
    (is (enabled? "#run-leibniz-btn"))
    (stop-browser)
    (stop-server server)))

(deftest run-leibniz-test
  (let [server (start-server)]
    (start-browser)
    (to test-base-url)
    (is (not (enabled? "#reset-leibniz-btn")))
    (run-euler-and-wait)
    (is (not (enabled? "#run-euler-btn")))
    (is (not (enabled? "#run-leibniz-btn")))
    (is (includes? (text "#leibniz-items-list") "4.0"))
    (is (enabled? "#reset-leibniz-btn"))
    (is (includes? (text "#leibniz-items-list") "2.8952380952380956"))
    (is (not (includes? (text "#leibniz-items-list") "3.2837384837384844")))
    (stop-euler)
    (is (includes? (text "body") "Scroll through list to see more results"))
    (is (not (enabled? "#reset-leibniz-btn")))
    (is (enabled? "#run-leibniz-btn"))
    (is (enabled? "#run-euler-btn"))
    (stop-browser)
    (stop-server server)))
