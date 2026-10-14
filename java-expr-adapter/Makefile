CC       = gcc
CFLAGS   = -O3
LDFLAGS  = -shared

PREFIX ?= /usr/local
#PREFIX = ${HOME}/.local
INCDIR = $(PREFIX)/include
LIBDIR = $(PREFIX)/lib

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

TARGET   := libjava-expr-adapter.$(LIB_EXT)
SRCS     := java-expr-adapter.c
HEADERS := org_dummy_calc_JavaExprAdapter.h
OBJS     := $(SRCS:.c=.o)

LDLIBS   = -lc-exprtk-adapter -lc-meval-adapter

.PHONY: all clean generate-jni-header install-native mvn-clean-package

all: clean generate-jni-header $(TARGET) install-native mvn-clean-package

$(TARGET): $(OBJS)
	$(CC) $(LDFLAGS) -o $@ $^ $(LDLIBS)

%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

clean:
	rm -f $(OBJS) $(TARGET)
	rm -rf target/

generate-jni-header:
	javac -h . src/main/java/org/dummy/calc/JavaExprAdapter.java
	rm src/main/java/org/dummy/calc/JavaExprAdapter.class

install-native:
	install -d $(INCDIR)
	install -d $(LIBDIR)
	install -m 644 $(HEADERS) $(INCDIR)/
	install -m 755 $(TARGET) $(LIBDIR)/

mvn-clean-package:
	mvn clean install
	SHARED_LIBRARY_HARNESS=FFM mvn clean install
