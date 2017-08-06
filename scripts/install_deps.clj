(require 'cljs.build.api)

(cljs.build.api/install-node-deps! {:react "15.6.1"
                                    :react-dom "15.6.1"
                                    :material-components-web "0.15.0"})
