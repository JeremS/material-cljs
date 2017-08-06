(require 'cljs.build.api)

(cljs.build.api/build "src"
  {:main           "material-cljs.core"
   :output-to      "resources/public/js/main.js"
   :output-dir     "resources/public/js/out"
   :asset-path     "js/out"
   :parallel-build true
   :verbose        true
   :pretty-print   true
   :optimizations  :none
   :language-in    :ecmascript6
   :language-out   :ecmascript3
   :npm-deps       {:react "15.6.1"
                    :react-dom "15.6.1"
                    :material-components-web "0.15.0"}})