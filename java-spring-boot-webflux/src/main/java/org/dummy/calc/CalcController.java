package org.dummy.calc;

import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static final Logger LOGGER = Logger.getLogger(CalcController.class.getSimpleName());
    public static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";
    private static final String NAN = "NaN";

    static {
        org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy");
    }

    @GetMapping()
    public Mono<String> get() {
        return Mono.just(WELCOME);
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<String> evalex(@RequestBody String expression) {
        String result = NAN;
        try {
            result = "" + new com.udojava.evalex.Expression(expression).eval();
        } catch (Exception e) {
            LOGGER.log(Level.INFO, null, e.getMessage());
        }
        return Mono.just(result);
    }

    @PostMapping(value = "/mxparser", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<String> mxparser(@RequestBody String expression) {
        String result = NAN;
        try {
            result = "" + new org.mariuszgromada.math.mxparser.Expression(expression).calculate();
        } catch (Exception e) {
            LOGGER.log(Level.INFO, null, e.getMessage());
        }
        return Mono.just(result);
    }

    @PostMapping(value = "/exprtk", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<String> exprtk(@RequestBody String expression) {
        String result = NAN;
        try {
            result = "" + JavaExprtkAdapter.calculate(expression);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, null, e.getMessage());
        }
        return Mono.just(result);
    }
}
