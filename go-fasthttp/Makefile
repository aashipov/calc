.PHONY: all

all: clean swagger test debug-build release-build

clean:
	rm -rf ./app
	rm -rf ./app-debug

swagger:
	swag init --output ./swagger

test:
	go test -v

debug-build:
	go build -gcflags "all=-N -l" -o app-debug

debug-run: debug-build
	dlv exec --listen=:2345 --headless --api-version=2 --accept-multiclient --continue ./app-debug

release-build:
	go build -o app

release-run: release-build
	./app
