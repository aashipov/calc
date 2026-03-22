package org.dummy.calc;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@RequestScoped
public class CalcResource {

    private static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";
    private static final String MXPARSER = "mxparser";
    private static final String EXPRTK = "exprtk";
    static final String NAN = "NaN";

    static {
        org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy");
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String welcome() {
        return WELCOME;
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String evalex(String expr) {
        String result = NAN;
        try {
            result = new com.udojava.evalex.Expression(expr).eval().toString();
        } catch (Exception e) {

        }
        return result;
    }

    @POST
    @Path(MXPARSER)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String mxparser(String expr) {
        String result = "" + new org.mariuszgromada.math.mxparser.Expression(expr).calculate();
        return result;
    }

    @POST
    @Path(EXPRTK)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String exprtk(String expr) {
        String result = NAN;
        if (expr != null) {
            result = String.valueOf(JavaExprtkAdapter.calculate(expr));
        }
        return result;
    }
}
