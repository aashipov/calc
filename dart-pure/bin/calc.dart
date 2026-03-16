import 'dart:convert';
import 'dart:isolate';

import 'package:calc/calc.dart' as calc;
import 'dart:io';
import 'dart:math';

const String welcome = "Welcome to calc service\nHTTP POST your expression";

void textResponse(HttpRequest req, String body) {
  req.response
    ..headers.set(HttpHeaders.contentTypeHeader, 'text/plain')
    ..write(body)
    ..close();
}

void handler(HttpRequest req) async {
  switch (req.method) {
    case 'POST':
      {
        String url = req.uri.toString();
        String expr = await utf8.decoder.bind(req).join();
        double result = calc.viaExprtk(expr);
        textResponse(req, result.toString());
      }
    default:
      textResponse(req, welcome);
  }
}

void worker(String message) async {
  HttpServer server = await HttpServer.bind("0.0.0.0", 8080, shared: true);
  await for (HttpRequest req in server) {
    handler(req);
  }
}

Future<void> main() async {
  var numberOfIsolates = max(1, Platform.numberOfProcessors) * 8;
  for (int ni = 0; ni < numberOfIsolates; ni++) {
    await Isolate.spawn(worker, "$ni");
  }
  await ProcessSignal.sigint.watch().first;
  print('SIGINT received, exiting.');
}
