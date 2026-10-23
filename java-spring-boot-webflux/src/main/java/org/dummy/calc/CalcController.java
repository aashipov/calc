package org.dummy.calc;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class CalcController {

    public static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";

    @GetMapping()
    public Mono<String> get() {
        return Mono.just(WELCOME);
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<String> evalex(@RequestBody(required = false) String expression) {
        String result = "" + JavaExprAdapterManaged.calculateViaEvalEx(expression);
        return Mono.just(result);
    }

    @PostMapping(path = "/" + JavaExprAdapter.EVALEX, consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<String> evalexEvalex(@RequestBody(required = false) String expression) {
        String result = "" + JavaExprAdapterManaged.calculateViaEvalEx(expression);
        return Mono.just(result);
    }

    @PostMapping(path = "/" + JavaExprAdapter.MXPARSER, consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<String> mxparser(@RequestBody(required = false) String expression) {
        String result = "" + JavaExprAdapterManaged.calculateViamXparser(expression);
        return Mono.just(result);
    }

    @PostMapping(path = "/" + JavaExprAdapter.EXPRTK, consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<String> exprtk(@RequestBody(required = false) String expression) {
        String result = "" + JavaExprAdapter.calculateViaExprtk(expression);
        return Mono.just(result);
    }

    @PostMapping(path = "/" + JavaExprAdapter.MEVAL, consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<String> meval(@RequestBody(required = false) String expression) {
        String result = "" + JavaExprAdapter.calculateViaMeval(expression);
        return Mono.just(result);
    }
}
