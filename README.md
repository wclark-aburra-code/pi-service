This is a web service that generates estimates of the irrational number Pi according to two different infinite series: the Leibniz series, and the Euler series. The two series are exposed as API endpoints that can generate streams of server-sent events. The API runs with the Pedestal library, using Clojure. Clojure was particularly well-suited to this task as lazy sequences are a primitive type, and can be passed as arguments to useful operations such as the reductions function and the doseq macro. Working with server-sent events in this context was a natural and painless experience.

There is a client-side view for testing this service, located at <localhost>/index.html. This HTML page includes Vue logic for consuming each stream of server-sent events, and loading the resultant data into the user interface.

Previously a client-side component was written in React to interact with this service. The Vue implementation has the advantage of using a comparatively lightweight JS framework. By allowing the JavaScript to exist in a single small HTML file without a dedicated static-file server, the client-side can be served from the same Pedestal server as the Clojure service, thus circumventing the common difficulties with cross-browser CORS configuration for server-sent events.

In order to run this application, please install Java, Clojure, and Leiningen (in that order). You can use "lein run" in the terminal, from the application's root directory, to start the Pedestal server. This will run both the Pi estimation service and the serving of the client-side HTML view at <localhost>/index.html.

Future challenges include parallelizing the Clojure Pi estimates, lowering the throttle rate, and tuning the core.async channel's buffer size accordingly; multi-channel communication for the server-sent events, to enable both algorithms to run and pass estimate messages to the client at the same time; maintaining state of the "reductions" lazy lists after connection reset; more explicit logic to close connections, as the current connection logic depends on Pedestal's automatic response to an EventSource being closed by the client.

<sub>
Previous implementations of the pi-estimation logic can be found here: https://github.com/billyclark3/clojurepi and here: https://github.com/billyclark3/goPi/blob/master/piEstimate.go. The Clojure logic is much improved in this version, particularly the use of lazy sequences.
</sub>
