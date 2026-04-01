package org.dummy.calc;

import java.util.logging.Level;
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
    public String evalex(@RequestBody String expr) {
        String result = NAN;
        try {
            result = new com.udojava.evalex.Expression(expr).eval().toString();
        } catch (Exception e) {
            LOGGER.log(Level.INFO, null, e);
        }
        return result;
    }

    @PostMapping(path = "/mxparser", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String mxparser(@RequestBody String expr) {
        String result = NAN;
        try {
            result = String.valueOf(new org.mariuszgromada.math.mxparser.Expression(expr).calculate());
        } catch (Exception e) {
            LOGGER.log(Level.INFO, null, e);
        }
        return result;
    }

    @PostMapping(path = "/exprtk", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String exprtk(@RequestBody String expr) {
        String result = NAN;
        try {
            result = String.valueOf(JavaExprtkAdapter.calculate(expr));
        } catch (Exception e) {
            LOGGER.log(Level.INFO, null, e);
        }
        return result;
    }
}
