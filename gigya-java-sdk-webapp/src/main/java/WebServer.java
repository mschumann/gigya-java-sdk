import com.gigya.socialize.GSObject;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WebServer extends HttpServlet {

    private static final long serialVersionUID = 7404141983054616442L;

    private final RequestParser _requestParser = new RequestParser();
    private final BloC _bloC = new BloC();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            throttle(req, resp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            throttle(req, resp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void throttle(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, JSONException {

        final ServletContext context = getServletContext();

        // enableAuthentication parameter.
        final String enableAuthentication = req.getParameter("enableAuthentication");

        PrintWriter out = resp.getWriter();
        resp.setContentType("text/json");

        // Enable CORS headers..
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST,GET,DELETE,PUT,OPTIONS");

        // Parse request parameters.
        final GSObject parameters = _requestParser.parse(req);
        // New request dispatcher.
        final RequestDispatcher _dispatcher = new RequestDispatcher(Boolean.parseBoolean(enableAuthentication));

        String jsonRes;

        final String servletPath = req.getServletPath();
        context.log("throttle servletPath: " + servletPath);

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
                // Fetch user account information.
                jsonRes = _dispatcher.getAccountInfo(parameters);
                context.log("getAccount response json: " + jsonRes);
                break;
            /*
            accounts.register endpoint.
             */
            case "/register":
                jsonRes = _dispatcher.register(parameters);
                context.log("register response json: " + jsonRes);
                break;
            /*
            accounts.login endpoint.
             */
            case "/login":
                jsonRes = _dispatcher.login(parameters);
                context.log("login response json: " + jsonRes);
                break;
            /*
            Verify JWT Token logic request.
             */
            case "/verifyIdToken":
                final boolean verified = _bloC.verifyJWTToken(parameters);
                if (verified) {
                    jsonRes = genericSuccess();
                } else {
                    jsonRes = genericError();
                }
                context.log("verifyIdToken response json: " + jsonRes);
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

    private String genericSuccess() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("errorCode", 0);
            jsonObject.put("apiVersion", 2);
            jsonObject.put("statusCode", 200);
            jsonObject.put("statusReason", "OK");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private String genericError() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("errorCode", 400009);
            jsonObject.put("apiVersion", 2);
            jsonObject.put("statusCode", 200);
            jsonObject.put("statusReason", "OK");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
