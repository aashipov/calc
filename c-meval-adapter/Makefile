PREFIX ?= /usr/local
#PREFIX = ${HOME}/.local
INCDIR = $(PREFIX)/include
LIBDIR = $(PREFIX)/lib

# Build profiles (default to release)
PROFILE      ?= release
CARGO_FLAGS  :=
ifeq ($(PROFILE),release)
    CARGO_FLAGS += --release
endif

# Detect OS for specific C ABI shared library extensions
ifeq ($(OS),Windows_NT)
    LIB_EXT := dll
else
    UNAME_S := $(shell uname -s)
    ifeq ($(UNAME_S),Linux)
        LIB_EXT := so
    else ifeq ($(UNAME_S),Darwin)
        LIB_EXT := dylib
    else
         $(error Unsupported operating system : '$(UNAME_S)')
    endif
endif

CARGO        := cargo
TARGET_DIR   := target
HEADERS := c-meval-adapter.h
LIB_FILE_NAME_ORIG := libc_meval_adapter.$(LIB_EXT)
LIB_FILE_NAME := libc-meval-adapter.$(LIB_EXT)
LIB_FILE_IN_TARGET := $(TARGET_DIR)/$(PROFILE)/$(LIB_FILE_NAME_ORIG)

.PHONY: all clean build test install uninstall

all: clean build test

clean:
	rm -rf $(TARGET_DIR)

build:
	$(CARGO) build $(CARGO_FLAGS)

test:
	$(CARGO) test $(CARGO_FLAGS)

install:
	install -d $(INCDIR)
	install -d $(LIBDIR)
	install -m 644 $(HEADERS) $(INCDIR)/
	install -m 755 $(LIB_FILE_IN_TARGET) $(LIBDIR)/$(LIB_FILE_NAME)

uninstall:
	rm $(INCDIR)/$(HEADERS) || true
	rm $(LIBDIR)/$(LIB_FILE_NAME) || true
