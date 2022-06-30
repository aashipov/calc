package org.dummy.calc;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/*")
public class CalcController {

    private static final Logger LOGGER = Logger.getLogger(CalcController.class.getSimpleName());
    public static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";

    public CalcController() {
        org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy");
    }

    @GetMapping()
    public Mono<String> get() {
        return Mono.just(WELCOME);
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<String> evalex(@RequestBody String expression) {
        try {
            return Mono.just(new com.udojava.evalex.Expression(expression).eval().toString());
        } catch (Exception e) {
            LOGGER.log(Level.INFO, null, e);
            return Mono.just(e.getMessage());
        }
    }

    @PostMapping(value = "/mxparser", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<String> mxparser(@RequestBody String expression) {
        return Mono.just(String.valueOf(new org.mariuszgromada.math.mxparser.Expression(expression).calculate()));
    }
}
