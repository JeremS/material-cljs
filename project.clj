(defproject material-cljs "0.1.0-SNAPSHOT"

  :description "FIXME: write description"

  :url "http://example.com/FIXME"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0-alpha17"]
                 [org.clojure/clojurescript "1.9.859"]
                 [figwheel-sidecar "0.5.12-SNAPSHOT"]]

  :target-path "target/%s"

  :source-paths ["src/main" "scripts"]

  :clean-targets ^{:protect false} ["resources/public/js/"
                                    "target"]
                                    ;"node_modules"]

  :plugins [[lein-cljsbuild "1.1.6"]]

  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.4"]
                                  [devcards "0.2.3"]]
                   :source-paths ["src/dev"]}}

  :cljsbuild {:builds [{:id           "dev"
                        :source-paths ["src/main" "src/dev"]
                        :figwheel     true
                        :compiler     {:main "material-cljs.core"
                                       :asset-path "js/dev_out"
                                       :output-to "resources/public/js/dev.js"
                                       :output-dir "resources/public/js/dev_out"
                                       :parallel-build true
                                       ;:verbose true
                                       :pretty-print true
                                       :optimizations :none
                                       :language-in :ecmascript6
                                       :language-out :ecmascript3
                                       :npm-deps {:react "15.6.1"
                                                  :react-dom "15.6.1"
                                                  :material-components-web "0.16.0"}
                                       :preloads [devtools.preload material-cljs.global]
                                       :external-config
                                       {:devtools/config
                                        {:features-to-install [:formatters :hints]}}}}]})