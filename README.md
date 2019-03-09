This is a web service that generates estimates of the irrational number Pi according to two different infinite series: the Leibniz series, and the Euler formulation. The two series are exposed as API endpoints that can generate streams of server-sent events.

There is a client-side view for testing this service, located at <localhost>/index.html. This HTML page includes Vue logic for consuming each stream of server-sent events, and loading the resultant data into the user interface.

In order to run this application, please install Java, Clojure, and Lein (in that order). You can use "lein run" in the terminal, from the application's root directory, to start the Pedestal server. This will run both the Pi estimation service and the serving of the client-side HTML view at <localhost>/index.html.
