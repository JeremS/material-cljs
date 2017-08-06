(require '[figwheel-sidecar.repl-api :as ra])



(ra/start-figwheel!) ;; <-- fetches configuration

(ra/switch-to-build "dev")

(ra/cljs-repl "dev")