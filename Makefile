PREFIX ?= ${HOME}/.local
RUNNABLE_TARGETS := build-baremetal test-baremetal native-client jmeter baremetal-load-test
JMETER := jmeter
JMETER_VERSION ?= 5.6.3
JMETER_TGZ_URL := "https://dlcdn.apache.org//jmeter/binaries/apache-$(JMETER)-$(JMETER_VERSION).tgz"
JMETER_TGZ_FILE := "apache-$(JMETER)-$(JMETER_VERSION).tgz"
CPP_FLAVORS := "cpp-Crow cpp-poco cpp-cpprestsdk"
JAVA_FLAFORS := "java-pure java-tomcat java-undertow java-jetty java-netty java-spring-boot-web java-spring-boot-webflux java-quarkus java-quarkus-reactive java-ktor java-helidon-se java-scala3-zio-http java-scala3-cask java-micronaut java-Vertx"
GO_FLAVORS := "go-pure go-fasthttp go-echo go-gin"
RUST_FLAVORS := "rust-actix rust-axum rust-ntex rust-tiny-http rust-rocket"
PYTHON_FLAVORS := "python-fastapi python-flask python-sanic python-tornado"
NODEJS_FLAVORS := "nodejs-expressjs nodejs-nestjs nodejs-pure"
.DEFAULT_GOAL := help
ALTERNATIVE_JS_FLAVORS := "bun-pure deno-oak deno-pure"

.PHONY: help clean-install clean-build build-baremetal test-baremetal flavor-baremetal-load-test baremetal-load-test

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
	$(MAKE) clean-build IMPLEMENTATIONS=$(CPP_FLAVORS)
	$(MAKE) clean-build IMPLEMENTATIONS=$(JAVA_FLAFORS)
	$(MAKE) clean-build IMPLEMENTATIONS=$(GO_FLAVORS)
	$(MAKE) clean-build IMPLEMENTATIONS=$(RUST_FLAVORS)
	$(MAKE) clean-build IMPLEMENTATIONS="dotnet"
	$(MAKE) clean-build IMPLEMENTATIONS=$(PYTHON_FLAVORS)
	$(MAKE) clean-build IMPLEMENTATIONS=$(NODEJS_FLAVORS)
	$(MAKE) clean-build IMPLEMENTATIONS=$(ALTERNATIVE_JS_FLAVORS)

test-baremetal:
	./test/post.sh
	./test/calc-test.sh
	./test/calc-benchmark.sh

native-client:
	$(MAKE) -C ./test/native-client clean
	$(MAKE) -C ./test/native-client
	./test/native-client/native-client

$(JMETER_TGZ_FILE):
	@if [ ! -f $(JMETER_TGZ_FILE) ]; then \
		echo "Downloading $(JMETER_TGZ_FILE)..."; \
		curl -L -o $(JMETER_TGZ_FILE) $(JMETER_TGZ_URL) ; \
	fi

$(JMETER): $(JMETER_TGZ_FILE)
	rm -rf $(JMETER)
	mkdir $(JMETER)
	tar -xzf $(JMETER_TGZ_FILE) -C $(JMETER) --strip-components=1
	cp -r ./test/. $(JMETER)


flavor-baremetal-load-test:
	for IMPLEMENTATION in $(IMPLEMENTATIONS); \
	do \
		$(MAKE) -C $$IMPLEMENTATION run & \
		    PID=$$! ;\
		echo "PID $$PID" ; \
	    IMPLEMENTATION=$$IMPLEMENTATION ./jmeter/jmeter-runner.sh ;\
		echo "Killing $$PID" ;\
		kill $$PID ;\
	done

baremetal-load-test:
	@if [ ! -d $(JMETER) ]; then \
		make $(JMETER) ;\
	fi
	$(MAKE) flavor-baremetal-load-test IMPLEMENTATIONS=$(CPP_FLAVORS)
	$(MAKE) flavor-baremetal-load-test IMPLEMENTATIONS=$(JAVA_FLAFORS)
	$(MAKE) flavor-baremetal-load-test IMPLEMENTATIONS=$(GO_FLAVORS)
	$(MAKE) flavor-baremetal-load-test IMPLEMENTATIONS=$(RUST_FLAVORS)
	$(MAKE) flavor-baremetal-load-test IMPLEMENTATIONS="dotnet"
	$(MAKE) flavor-baremetal-load-test IMPLEMENTATIONS=$(PYTHON_FLAVORS)
	$(MAKE) flavor-baremetal-load-test IMPLEMENTATIONS=$(NODEJS_FLAVORS)
	$(MAKE) flavor-baremetal-load-test IMPLEMENTATIONS=$(ALTERNATIVE_JS_FLAVORS)
	./$(JMETER)/stats/do-stats.sh
