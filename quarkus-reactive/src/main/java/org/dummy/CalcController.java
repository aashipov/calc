package org.dummy;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/")
public class CalcController {

    private static final Logger LOGGER = Logger.getLogger(CalcController.class.getSimpleName());
    public static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String welcome() {
        return WELCOME;
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String evalex(String expression) {
        try {
            return new com.udojava.evalex.Expression(expression).eval().toString();
        } catch (Exception e) {
            LOGGER.log(Level.INFO, null, e);
            return e.getMessage();
        }
    }

    @POST
    @Path("/mxparser")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String mxparser(String expression) {
        return String.valueOf(new org.mariuszgromada.math.mxparser.Expression(expression).calculate());
    }
}