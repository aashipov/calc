package org.dummy.calc;

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

    public static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String welcome() {
        return WELCOME;
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String evalex(String expr) {
        return "" + JavaExprAdapterManaged.calculateViaEvalEx(expr);
    }

    @POST
    @Path("/" + JavaExprAdapter.EVALEX)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String evalexEvalex(String expr) {
        return "" + JavaExprAdapterManaged.calculateViaEvalEx(expr);
    }

    @POST
    @Path("/" + JavaExprAdapter.MXPARSER)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String mxparser(String expr) {
        return "" + JavaExprAdapterManaged.calculateViamXparser(expr);
    }

    @POST
    @Path("/" + JavaExprAdapter.EXPRTK)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String exprtk(String expr) {
        return "" + JavaExprAdapter.calculateViaExprtk(expr);
    }

    @POST
    @Path("/" + JavaExprAdapter.MEVAL)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String meval(String expr) {
        return "" + JavaExprAdapter.calculateViaMeval(expr);
    }
}
