import os
from wsgiref.simple_server import WSGIServer, make_server

from pyramid.config import Configurator
from pyramid.request import Request
from pyramid.response import Response
from pyramid.router import Router

from src.c_exprtk_adapter import calculate_via_exprtk

WELCOME: str = "Welcome to calc service\nHTTP POST your expression\n"
NAN = "nan"
TEXT_PLAIN: str = "text/plain"
HTTP_PORT: int = int(os.getenv("HTTP_PORT", 8080))


def catchall(request: Request) -> Response:
    if "POST".__eq__(request.method):
        expr: str = request.text
        result: str = calculate_via_exprtk(expr)
        return Response(result, content_type=TEXT_PLAIN)
    else:
        return Response(WELCOME, content_type=TEXT_PLAIN)


def create_calc() -> Router:
    cfg: Configurator = Configurator()
    cfg.add_route("catchall", "/*subpath")
    cfg.add_view(catchall, route_name="catchall")
    return cfg.make_wsgi_app()


app: Router = create_calc()


if __name__ == "__main__":
    srv: WSGIServer = make_server("0.0.0.0", HTTP_PORT, app)
    srv.serve_forever()
