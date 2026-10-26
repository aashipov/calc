.PHONY: all check-dlv check-swag clean swagger test debug-build release-build debug-run release-run

all: swagger test debug-build release-build

check-dlv:
	@if ! dlv --help > /dev/null 2>&1 ; \
	then \
	    printf 'Installing dlv\n' ; \
		go install github.com/go-delve/delve/cmd/dlv@latest ; \
	fi

check-swag:
	@if ! swag --help > /dev/null 2>&1 ; \
	then \
	    printf 'Installing swag\n' ; \
		go install github.com/swaggo/swag/cmd/swag@latest ; \
	fi

clean:
	rm -rf ./app
	rm -rf ./app-debug

swagger: check-swag
	swag init --output ./swagger

test:
	go test -v

debug-build: check-dlv
	go build -gcflags "all=-N -l" -o app-debug

debug-run: debug-build
	dlv exec --listen=:2345 --headless --api-version=2 --accept-multiclient --continue ./app-debug

release-build:
	go build -o app

release-run: release-build
	./app
