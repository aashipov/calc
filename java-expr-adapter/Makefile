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

ADAPTER_NAME := java-expr-adapter-jni

TARGET   := lib$(ADAPTER_NAME).$(LIB_EXT)
SRCS     := $(ADAPTER_NAME).c
HEADERS := org_dummy_calc_JavaExprAdapterJni.h
OBJS     := $(SRCS:.c=.o)

LDLIBS   = -lc-exprtk-adapter -lc-meval-adapter

JAVA_BUILD_CMD := mvn install

.PHONY: all clean generate-jni-header install-native $(JAVA_BUILD_CMD)

all: clean generate-jni-header $(TARGET) install-native $(JAVA_BUILD_CMD)

$(TARGET): $(OBJS)
	$(CC) $(LDFLAGS) -o $@ $^ $(LDLIBS)

%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

clean:
	rm -f $(OBJS) $(TARGET)
	rm -rf target/

generate-jni-header:
	javac -h . src/main/java/org/dummy/calc/JavaExprAdapterJni.java
	rm src/main/java/org/dummy/calc/JavaExprAdapterJni.class

install-native:
	install -d $(INCDIR)
	install -d $(LIBDIR)
	install -m 644 $(HEADERS) $(INCDIR)/
	install -m 755 $(TARGET) $(LIBDIR)/

$(JAVA_BUILD_CMD):
	$(JAVA_BUILD_CMD)
	SHARED_LIBRARY_HARNESS=FFM $(JAVA_BUILD_CMD)
