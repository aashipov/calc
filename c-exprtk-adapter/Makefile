PREFIX ?= /usr/local
#PREFIX=${HOME}/.local
INCDIR = $(PREFIX)/include
LIBDIR = $(PREFIX)/lib
PROFILE      ?= Release
EXPRTK_HEADER := exprtk.hpp

.PHONY: all build clean download-exprtk-header install

all: download-exprtk build

build:
	cmake -S . -B $(PROFILE) -DCMAKE_INSTALL_PREFIX=$(PREFIX) -DCMAKE_BUILD_TYPE=$(PROFILE)
	cmake --build $(PROFILE)

clean:
	rm -rf Release/ Debug/ || true

download-exprtk:
	@if [ ! -e $(INCDIR)/$(EXPRTK_HEADER) ] ; \
	then \
		curl -L -o $(INCDIR)/$(EXPRTK_HEADER) https://raw.githubusercontent.com/ArashPartow/exprtk/refs/heads/master/exprtk.hpp ; \
	fi

install:
	cmake --install $(PROFILE)
