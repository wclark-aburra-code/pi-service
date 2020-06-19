
(defproject server-sent-events "0.0.0"
      :description "Pi-estimation service"
      :license {:name "Eclipse Public License"
                :url "http://www.eclipse.org/legal/epl-v10.html"}
      :dependencies [[org.clojure/clojure "1.8.0"]
                     [io.pedestal/pedestal.service "0.5.1"]
    
                     [io.pedestal/pedestal.jetty "0.5.1"]
                     ;; [io.pedestal/pedestal.tomcat "0.5.1"]
                     ;; Logging
                     [ch.qos.logback/logback-classic "1.1.7" :exclusions [[org.slf4j/slf4j-api]]]
                     [org.slf4j/jul-to-slf4j "1.7.21"]
                     [org.slf4j/jcl-over-slf4j "1.7.21"]
                     [org.slf4j/log4j-over-slf4j "1.7.21"]
    
                     ;; Deps cleanup
                     [org.clojure/tools.analyzer.jvm "0.6.10"]
                     [org.clojure/tools.reader "1.0.0-beta2"]
    
                     ;; Example CLJS client
                     [org.clojure/core.async "0.2.391"]
                     [org.clojure/clojurescript "1.9.229" :exclusions [org.clojure/tools.reader]]
    
                     ]
      :plugins [[lein-cljsbuild "1.1.3"]]
      :min-lein-version "2.0.0"
      :pedantic? :abort
      :resource-paths ["resources" "config"]
      :global-vars  {*warn-on-reflection* true
                     *assert* true}
      :main ^{:skip-aot true} server-sent-events.server
      :profiles {:dev {:aliases {"run-dev" ["trampoline" "run" "-m" "server-sent-events.server/run-dev"]}
                       :dependencies []
                       :source-paths ["dev"]}}
      :cljsbuild {:builds
                  {:adv {:source-paths  ["src" "target/classes"]
                        :compiler
                        {:output-dir "target/out"
                         :output-to "resources/public/js/app.js"
                         :pretty-print false
                         :optimizations :advanced}}}})
    