PREFIX ?= /usr/local
#PREFIX=${HOME}/.local
INCDIR = $(PREFIX)/include
LIBDIR = $(PREFIX)/lib
PROFILE      ?= Release
HTTPLIB_HEADER := httplib.h

.PHONY: all build clean download-httplib test install run

all: build test

build: download-httplib
	cmake -S . -B $(PROFILE) -DCMAKE_INSTALL_PREFIX=$(PREFIX) -DCMAKE_BUILD_TYPE=$(PROFILE) -DENABLE_TESTS=On
	cmake --build $(PROFILE)

clean:
	rm -rf Release/ Debug/ || true

download-httplib:
	@if [ ! -e $(INCDIR)/$(HTTPLIB_HEADER) ] ; \
	then \
		curl -L -o $(INCDIR)/$(HTTPLIB_HEADER) https://raw.githubusercontent.com/yhirose/cpp-httplib/refs/heads/master/httplib.h ; \
	fi

test: build
	ctest --test-dir $(PROFILE)/tests/

install:
	cmake --install $(PROFILE)

run:
	$(PROFILE)/calc
