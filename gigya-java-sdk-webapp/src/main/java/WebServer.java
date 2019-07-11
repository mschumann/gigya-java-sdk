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
    private RequestDispatcher _dispatcher;

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

        // enableAuthentication parameter.
        final String enableAuthentication = req.getParameter("enableAuthentication");
        _dispatcher = new RequestDispatcher(Boolean.parseBoolean(enableAuthentication));
        _dispatcher.setContext(context);

        PrintWriter out = resp.getWriter();
        resp.setContentType("text/json");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST,GET,DELETE,PUT,OPTIONS");

        final String servletPath = req.getServletPath();

        context.log("throttle servletPath: " + servletPath);

        String jsonRes;
        switch (servletPath) {
            /*
            accounts.getJWTPublicKey endpoint.
             */
            case "/getJWTPublicKey":

                // Fetch site public key.
                jsonRes = _dispatcher.getJWTPublicKey();
                context.log("getJWTPublicKey response json: " + jsonRes);

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

                // Currently all fields are required.
                final String photoURL = req.getParameter("photoURL");
                final String firstName = req.getParameter("firstName");
                final String lastName = req.getParameter("lastName");

                context.log("register with parameters:" +
                        " email " + registrationEmail +
                        " password: " + registrationPassword +
                        " exp: " + exp +
                        " photoURL: " + photoURL);

                // Initiate registration request.
                final String profileJson = "{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"photoURL\":\"" + photoURL + "\"}";
                jsonRes = _dispatcher.register(registrationEmail, registrationPassword, Integer.parseInt(exp), profileJson);
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
