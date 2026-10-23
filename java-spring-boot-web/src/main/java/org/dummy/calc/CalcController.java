package org.dummy.calc;

import java.util.logging.Logger;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalcController {

    private static final Logger LOGGER = Logger.getLogger(CalcController.class.getSimpleName());
    private static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";
    private static final String NAN = "NaN";

    @GetMapping(path = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String welcome() {
        return WELCOME;
    }

    @PostMapping(path = "/", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String evalex(@RequestBody(required = false) String expr) {
        return "" + JavaExprAdapterManaged.calculateViaEvalEx(expr);
    }

    @PostMapping(path = "/" + JavaExprAdapter.EVALEX, consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String evalexEvalex(@RequestBody(required = false) String expr) {
        return "" + JavaExprAdapterManaged.calculateViaEvalEx(expr);
    }

    @PostMapping(path = "/" + JavaExprAdapter.MXPARSER, consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String mxparser(@RequestBody(required = false) String expr) {
        return "" + JavaExprAdapterManaged.calculateViamXparser(expr);
    }

    @PostMapping(path = "/" + JavaExprAdapter.EXPRTK, consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String exprtk(@RequestBody(required = false) String expr) {
        return "" + JavaExprAdapter.calculateViaExprtk(expr);
    }

    @PostMapping(path = "/" + JavaExprAdapter.MEVAL, consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String meval(@RequestBody(required = false) String expr) {
        return "" + JavaExprAdapter.calculateViaMeval(expr);
    }
}
