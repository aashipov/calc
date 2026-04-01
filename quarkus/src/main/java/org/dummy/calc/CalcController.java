package org.dummy.calc;

import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@ApplicationScoped
public class CalcController {

    private static final Logger LOGGER = Logger.getLogger(CalcController.class.getSimpleName());
    public static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";
    private static final String NAN = "NaN";

    public CalcController() {
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
            LOGGER.log(Level.INFO, null, e);
        }
        return result;
    }

    @POST
    @Path("/mxparser")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String mxparser(String expr) {
        String result = NAN;
        try {
            result = String.valueOf(new org.mariuszgromada.math.mxparser.Expression(expr).calculate());
        } catch (Exception e) {
            LOGGER.log(Level.INFO, null, e);
        }
        return result;
    }

    @POST
    @Path("/exprtk")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String exprtk(String expr) {
        String result = NAN;
        try {
            result = String.valueOf(JavaExprtkAdapter.calculate(expr));
        } catch (Exception e) {
            LOGGER.log(Level.INFO, null, e);
        }
        return result;
    }
}
