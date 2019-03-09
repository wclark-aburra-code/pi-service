(ns server-sent-events.client.main
  (:require [cljs.core.async :as async])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(defn data-identity [e]
  (.-data e))

(defn event-source!
  ([url-or-src]
   (event-source! url-or-src (async/chan 10) data-identity "message"))
  ([url-or-src ch]
   (event-source! url-or-src ch data-identity "message"))
  ([url-or-src ch f]
   (event-source! url-or-src ch f "message"))
  ([url-or-src ch f msg-type]
   (let [src (if (instance? js/EventSource url-or-src)
               url-or-src
               (js/EventSource. url-or-src))]
     (.addEventListener src
                        msg-type
                        (fn [e]
                          (async/put! ch (f e)))
                        false)
     {:source src
      :channel ch})))

;; Set up the channel
(def sse-chan (async/chan 10))
(def sse-events (event-source! "/" sse-chan data-identity "count"))


(.log js/console "Starting to yield events...")
(go-loop [event (async/<! sse-chan)]
  (if event
    (do (.log js/console "New SSE event:" event)
        (recur (async/<! sse-chan)))
    (.log js/console "No more events; Event channel is closed")))
