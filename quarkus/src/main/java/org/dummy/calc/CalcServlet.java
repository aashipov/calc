package org.dummy.calc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CalcServlet", urlPatterns = "/*", loadOnStartup = 1)
public class CalcServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CalcServlet.class.getSimpleName());
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String MXPARSER = "mxparser";
    private static final String EXPRTK = "exprtk";

    public CalcServlet() {
        org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        textResponse(resp, HttpServletResponse.SC_OK, "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (InputStream inputStream = req.getInputStream(); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            inputStream.transferTo(byteArrayOutputStream);
            String expr = byteArrayOutputStream.toString(DEFAULT_CHARSET);
            String result;
            if (req.getRequestURI().toString().contains(MXPARSER)) {
                result = String.valueOf(new org.mariuszgromada.math.mxparser.Expression(expr).calculate());
            } else if (req.getRequestURI().toString().contains(EXPRTK)) {
                result = "" + JavaExprtkAdapter.calculate(expr);
            } else {
                result = (new com.udojava.evalex.Expression(expr).eval()).toString();
            }
            textResponse(resp, HttpServletResponse.SC_OK, result);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Can not evaluate {0}", e.getMessage());
            textResponse(resp, HttpServletResponse.SC_OK, e.getMessage());
        }
    }

    static void textResponse(HttpServletResponse response, int statusCode, String msg) {
        response.setStatus(statusCode);
        try (PrintWriter pw = response.getWriter()) {
            pw.print(msg);
            pw.flush();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Can send text response {0}", e.getMessage());
        }
    }
}
