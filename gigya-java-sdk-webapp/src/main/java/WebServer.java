import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WebServer extends HttpServlet {

    /*
    GSRequest dispatcher.
     */
    final private RequestDispatcher _dispatcher = new RequestDispatcher(true);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            throttle(req, resp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            throttle(req, resp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void throttle(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException, JSONException {

        final ServletContext context = getServletContext();

        PrintWriter out = resp.getWriter();
        resp.setContentType("text/plain");

        final String servletPath = req.getServletPath();

        context.log("throttle servletPath: " + servletPath);

        String jsonRes;
        switch (servletPath) {
            /*
            accounts.getJWTPublicKey endpoint.
             */
            case "/getJWTPublicKey":

                // Fetch site public key.
                jsonRes = _dispatcher.getPublicKey();
                context.log("getPublicKey response json: " + jsonRes);

                break;
            /*
             accounts.getAccountInfo endpoint.
             */
            case "/getAccountInfo":

                // Fetch UID from request parameters.
                final String uid = req.getParameter("UID");
                // Fetch user account information.
                jsonRes = _dispatcher.getAccountInfo(uid);
                context.log("getAccount response json: " + jsonRes);

                break;
            /*
            accounts.register endpoint.
             */
            case "/register":

                // Fetch registration parameters.
                final String registrationEmail = req.getParameter("email");
                final String registrationPassword = req.getParameter("password");
                final String exp = req.getParameter("exp");

                context.log("register with parameters: email " + registrationEmail + " password: " + registrationPassword + " exp: " + exp);

                // Initiate registration request.
                jsonRes = _dispatcher.register(registrationEmail, registrationPassword, Integer.parseInt(exp));
                context.log("register response json: " + jsonRes);

                break;
            /*
            accounts.login endpoint.
             */
            case "/login":

                // Fetch login parameters.
                final String loginId = req.getParameter("loginId");
                final String loginPassword = req.getParameter("password");

                context.log("login with parameters: loginId " + loginId + " password: " + loginPassword);

                // Request login.
                jsonRes = _dispatcher.login(loginId, loginPassword);
                context.log("login response json: " + jsonRes);

                break;
            default:
                jsonRes = "{ \"error\": \"Unsupported error\"}";
                break;
        }

        // Just for fun. Pretty print the JSON response.
        final JSONObject jsonObject = new JSONObject(jsonRes);
        out.write(jsonObject.toString(4));

        out.close();
    }
}
