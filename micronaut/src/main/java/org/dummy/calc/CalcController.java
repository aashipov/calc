package org.dummy.calc;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.runtime.http.scope.RequestScope;

@Controller
@RequestScope
public class CalcController {

    private static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";
    private static final String MXPARSER = "mxparser";
    private static final String EXPRTK = "exprtk";
    static final String NAN = "NaN";

    static {
        org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy");
    }

    @Get(value = UriMapping.DEFAULT_URI, produces = MediaType.TEXT_PLAIN)
    public String welcome() {
        return WELCOME;
    }

    @Post(value = UriMapping.DEFAULT_URI, consumes = MediaType.TEXT_PLAIN, produces = MediaType.TEXT_PLAIN)
    public String evalex(@Body String expr) {
        String result = NAN;
        try {
            result = new com.udojava.evalex.Expression(expr).eval().toString();
        } catch (Exception e) {

        }
        return result;
    }

    @Post(value = UriMapping.DEFAULT_URI + MXPARSER, consumes = MediaType.TEXT_PLAIN, produces = MediaType.TEXT_PLAIN)
    public String mxparser(@Body String expr) {
        String result = "" + new org.mariuszgromada.math.mxparser.Expression(expr).calculate();
        return result;
    }

    @Post(value = UriMapping.DEFAULT_URI + EXPRTK, consumes = MediaType.TEXT_PLAIN, produces = MediaType.TEXT_PLAIN)
    public String exprtk(@Body String expr) {
        String result = NAN;
        if (expr != null) {
            result = String.valueOf(JavaExprtkAdapter.calculate(expr));
        }
        return result;
    }
}
