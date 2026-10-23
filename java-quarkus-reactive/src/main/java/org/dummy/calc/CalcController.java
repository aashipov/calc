package org.dummy.calc;

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

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> welcome() {
        return Uni.createFrom().item(WELCOME);
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> evalex(String expression) {
        String result = "" + JavaExprAdapterManaged.calculateViaEvalEx(expression);
        return Uni.createFrom().item(result);
    }

    @POST
    @Path("/" + JavaExprAdapter.EVALEX)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> evalexEvalex(String expression) {
        String result = "" + JavaExprAdapterManaged.calculateViaEvalEx(expression);
        return Uni.createFrom().item(result);
    }

    @POST
    @Path("/" + JavaExprAdapter.MXPARSER)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> mxparser(String expression) {
        String result = "" + JavaExprAdapterManaged.calculateViamXparser(expression);
        return Uni.createFrom().item(result);
    }

    @POST
    @Path("/" + JavaExprAdapter.EXPRTK)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> exprtk(String expression) {
        String result = "" + JavaExprAdapter.calculateViaExprtk(expression);
        return Uni.createFrom().item(result);
    }

    @POST
    @Path("/" + JavaExprAdapter.MEVAL)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> meval(String expression) {
        String result = "" + JavaExprAdapter.calculateViaMeval(expression);
        return Uni.createFrom().item(result);
    }
}
