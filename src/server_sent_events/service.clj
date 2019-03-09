;; Service that yields estimates of the irrational number pi, via server-sent events
(ns server-sent-events.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.sse :as sse]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [ring.util.response :as ring-resp]
            [clojure.core.async :as async]
  )
)

(defn seq-of-terms
   [func]
   (map func (iterate (partial + 1) 0))
)

(defn euler-term [n]
  (let [current (+ n 1)] (/ 6.0 (* current current)))
)

; The following returns a lazy list representing iterable sums that estimate pi
; according to the Euler series for increasing amounts of terms in the series.
; Sample usage: (take 100 euler-reductions)
(def euler-reductions
  (map (fn [sum] (Math/sqrt sum))  (reductions + (seq-of-terms euler-term) ))
)

(defn leibniz-term [n] ; starts at zero
   (let [
          oddnum (+ (* 2.0 n) 1.0)
          signfactor (- 1 (* 2 (mod n 2)))
        ]
        (/ (* 4.0 signfactor) oddnum)
  )
)

; The following returns a lazy list representing iterable sums that estimate pi
; according to the Leibniz series for increasing amounts of terms in the series.
; Sample usage: (take 100 leibniz-reductions)
(def leibniz-reductions (reductions + (seq-of-terms leibniz-term)))

(defn send-result
  [event-ch count-num rdcts]
  (doseq [item rdcts]
    (Thread/sleep 150) ; we must use a naive throttle here to prevent an overflow on the core.async CSP channel, event-ch
    (async/put! event-ch (str item))
  )
)

(defn sse-euler-stream-ready
  "Start to send estimates to the client according to the Euler series"
  [event-ch ctx]
  ;; The context is passed into this function.
  (let
    [
      {:keys [request response-channel]} ctx
      lazy-list euler-reductions
    ]
    (send-result event-ch 10 lazy-list)
  )
)

(defn sse-leibniz-stream-ready
  "Start to send estimates to the client according to the Leibniz series"
  [event-ch ctx]
  (let
    [
      {:keys [request response-channel]} ctx
      lazy-list leibniz-reductions
    ]
    (send-result event-ch 10 lazy-list)
  )
)

(defn test-response-page
  [request]
  (ring-resp/response "Server Sent Service - Test response"))

;; Wire root URL to sse event stream
;; with custom event-id setting
(defroutes routes
  [[["/" {:get [::send-result-euler (sse/start-event-stream sse-euler-stream-ready)]}
    ["/euler" {:get [::send-result
                    (sse/start-event-stream sse-euler-stream-ready)]}]
    ["/leibniz" {:get [::send-result-leibniz
                      (sse/start-event-stream sse-leibniz-stream-ready)]}]
    ["/test-response" {:get test-response-page}]]]])

(def url-for (route/url-for-routes routes))

(def service {:env :prod
              ::http/routes routes
              ;; Root for resource interceptor that is available by default.
              ::http/resource-path "/public"
              ;; Either :jetty or :tomcat (see comments in project.clj
              ;; to enable Tomcat)
              ::http/type :jetty
              ::http/port 8080
              ;;::http/allowed-origins ["http://127.0.0.1:8081"]
              }
)
