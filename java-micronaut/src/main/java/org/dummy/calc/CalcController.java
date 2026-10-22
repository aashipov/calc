package org.dummy.calc;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.runtime.http.scope.RequestScope;

@Controller
@RequestScope
public class CalcController {

    private static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";

    @Get(value = UriMapping.DEFAULT_URI, produces = MediaType.TEXT_PLAIN)
    public String welcome() {
        return WELCOME;
    }

    @Post(value = UriMapping.DEFAULT_URI, consumes = MediaType.TEXT_PLAIN, produces = MediaType.TEXT_PLAIN)
    public String evalex(@Nullable @Body String expr) {
        return "" + JavaExprAdapterManaged.calculateViaEvalEx(expr);
    }

    @Post(value = UriMapping.DEFAULT_URI + JavaExprAdapter.EVALEX, consumes = MediaType.TEXT_PLAIN, produces = MediaType.TEXT_PLAIN)
    public String evalexEvalex(@Nullable @Body String expr) {
        return "" + JavaExprAdapterManaged.calculateViaEvalEx(expr);
    }

    @Post(value = UriMapping.DEFAULT_URI + JavaExprAdapter.MXPARSER, consumes = MediaType.TEXT_PLAIN, produces = MediaType.TEXT_PLAIN)
    public String mxparser(@Nullable @Body String expr) {
        return "" + JavaExprAdapterManaged.calculateViamXparser(expr);
    }

    @Post(value = UriMapping.DEFAULT_URI + JavaExprAdapter.EXPRTK, consumes = MediaType.TEXT_PLAIN, produces = MediaType.TEXT_PLAIN)
    public String exprtk(@Nullable @Body String expr) {
        return "" + JavaExprAdapter.calculateViaExprtk(expr);
    }

    @Post(value = UriMapping.DEFAULT_URI + JavaExprAdapter.MEVAL, consumes = MediaType.TEXT_PLAIN, produces = MediaType.TEXT_PLAIN)
    public String meval(@Nullable @Body String expr) {
        return "" + JavaExprAdapter.calculateViaMeval(expr);
    }
}
