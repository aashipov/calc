DOT_VENV := .venv

.PHONY = clean distro enter-venv test run debug

all: test distro

clean:
	rm -rf $(DOT_VENV)
	find . -type f -name '*.pyc' -delete
	find . -type d -name '__pycache__' -delete
	rm -rf .pytest_cache .status

distro: enter-venv
	$(DOT_VENV)/bin/python3 -m build --wheel

enter-venv:
	@if [ ! -d $(DOT_VENV) ] ; \
	then \
	    python3 -m venv $(DOT_VENV) ; \
		. $(DOT_VENV)/bin/activate  ; \
		pip3 install -e . ; \
	else \
		. $(DOT_VENV)/bin/activate  ; \
	fi

test: enter-venv
	$(DOT_VENV)/bin/python3 -m pytest -v

run: enter-venv
	$(DOT_VENV)/bin/uvicorn --log-level error --host 0.0.0.0 --port 8080 --workers 4 --app-dir src app:app

debug: enter-venv
	$(DOT_VENV)/bin/python3 -m debugpy --listen 0.0.0.0:5678 src/app.py
