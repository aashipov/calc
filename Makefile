PREFIX ?= ${HOME}/.local

RUNNABLE_TARGETS := build-baremetal test-baremetal native-client

.DEFAULT_GOAL := help

.PHONY: help clean-install clean-build build-baremetal test-baremetal

help:
	@echo "Available targets: $(RUNNABLE_TARGETS)"

clean-install:
	for implementation in $(IMPLEMENTATIONS); \
	do \
	    $(MAKE) -C $$implementation clean; \
		 PREFIX=$(PREFIX) $(MAKE) -C $$implementation install; \
	done

clean-build:
	for implementation in $(IMPLEMENTATIONS); \
	do \
	    $(MAKE) -C $$implementation clean; \
		$(MAKE) -C $$implementation; \
	done

build-baremetal:
	$(MAKE) clean-install IMPLEMENTATIONS="c-exprtk-adapter c-meval-adapter java-expr-adapter"
	$(MAKE) clean-build IMPLEMENTATIONS="cpp-Crow cpp-poco cpp-cpprestsdk"
	$(MAKE) clean-build IMPLEMENTATIONS="java-pure java-tomcat java-undertow java-jetty java-netty java-spring-boot-web java-spring-boot-webflux java-quarkus java-quarkus-reactive java-ktor java-helidon-se java-scala3-zio-http java-scala3-cask java-micronaut java-Vertx"
	$(MAKE) clean-build IMPLEMENTATIONS="go-pure go-fasthttp go-echo go-gin"
	$(MAKE) clean-build IMPLEMENTATIONS="rust-actix rust-axum rust-ntex rust-tiny-http rust-rocket"
	$(MAKE) clean-build IMPLEMENTATIONS="dotnet"
	$(MAKE) clean-build IMPLEMENTATIONS="python-fastapi python-flask python-sanic python-tornado"
	$(MAKE) clean-build IMPLEMENTATIONS="nodejs-expressjs nodejs-nestjs nodejs-pure"
	$(MAKE) clean-build IMPLEMENTATIONS="bun-pure deno-oak deno-pure"

test-baremetal:
	./test/post.sh
	./test/calc-test.sh
	./test/calc-benchmark.sh

native-client:
	$(MAKE) -C ./test/native-client clean
	$(MAKE) -C ./test/native-client
	./test/native-client/native-client
