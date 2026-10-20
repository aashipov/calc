NPM := npm
RUN := run
NODE_MODULES := node_modules
SERVER_JS := server.js

.PHONY: all install build test clean dev run

all: build test

clean:
	rm -rf $(NODE_MODULES)
	rm $(SERVER_JS)

$(NODE_MODULES):
	$(NPM) install

build: $(NODE_MODULES)
	$(NPM) $(RUN) build

test: $(NODE_MODULES)
	$(NPM) $(RUN) test

dev: $(NODE_MODULES)
	$(NPM) $(RUN) dev

run: build
	$(NPM) $(RUN) start
