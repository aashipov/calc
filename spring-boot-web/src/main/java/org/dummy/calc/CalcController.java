package org.dummy.calc;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController()
public class CalcController {

    private static final Logger LOGGER = Logger.getLogger(CalcController.class.getSimpleName());

    @GetMapping()
    public String welcome() {
        return "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String evalex(@RequestBody String expression) {
        try {
            return new com.udojava.evalex.Expression(expression).eval().toString();
        } catch (Exception e) {
            LOGGER.log(Level.INFO, null, e);
            return e.getMessage();
        }
    }

    @PostMapping(value = "/mxparser", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String mxparser(@RequestBody String expression) {
        return String.valueOf(new org.mariuszgromada.math.mxparser.Expression(expression).calculate());
    }
}
