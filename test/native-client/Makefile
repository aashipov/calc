CC       = gcc
CFLAGS   = -O3

TARGET   = native-client
SRCS     = main.c
OBJS     = $(SRCS:.c=.o)

LDLIBS   = -lc-exprtk-adapter -lc-meval-adapter

.PHONY: all clean

all: clean $(TARGET)

$(TARGET): $(OBJS)
	$(CC) $(LDFLAGS) -o $@ $^ $(LDLIBS)

%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

clean:
	rm -f $(OBJS) $(TARGET)
