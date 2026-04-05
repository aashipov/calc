package org.dummy.calc;

import java.util.logging.Level;
import java.util.logging.Logger;

import io.smallrye.mutiny.Uni;
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

    static {
        org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy");
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> welcome() {
        return Uni.createFrom().item(WELCOME);
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> evalex(String expression) {
        String result = NAN;
        try {
            result = "" + new com.udojava.evalex.Expression(expression).eval();
        } catch (Exception e) {
            LOGGER.log(Level.INFO, null, e);
        }
        return Uni.createFrom().item(result);
    }

    @POST
    @Path("/mxparser")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> mxparser(String expression) {
        String result = NAN;
        try {
            result = "" + new org.mariuszgromada.math.mxparser.Expression(expression).calculate();
        } catch (Exception e) {
            LOGGER.log(Level.INFO, null, e);
        }
        return Uni.createFrom().item(result);
    }

    @POST
    @Path("/exprtk")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> exprtk(String expression) {
        String result = NAN;
        try {
            result = "" + JavaExprtkAdapter.calculate(expression);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, null, e);
        }
        return Uni.createFrom().item(result);
    }
}
