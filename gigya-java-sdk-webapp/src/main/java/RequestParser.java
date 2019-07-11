import com.gigya.socialize.GSObject;

import javax.servlet.http.HttpServletRequest;

class RequestParser {

    GSObject parse(HttpServletRequest req) {
        final String servletPath = req.getServletPath();
        switch (servletPath) {
            case "/register":
                return parseRegisterParameters(req);
            case "/login":
                return parseLoginParameters(req);
            case "/getAccountInfo":
                return parseGetAccountInfoParameters(req);
            case "/verifyIdToken":
                return parseVerifyJWTToken(req);
            default:
                break;
        }
        return null;
    }

    private GSObject parseRegisterParameters(HttpServletRequest req) {
        final GSObject params = new GSObject();

        // Parse main registration fields.
        final String registrationEmail = req.getParameter("email");
        final String registrationPassword = req.getParameter("password");
        final String exp = req.getParameter("exp");

        // Parse profile fields.
        final String photoURL = req.getParameter("photoURL");
        final String firstName = req.getParameter("firstName");
        final String lastName = req.getParameter("lastName");
        final String profileJson = "{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"photoURL\":\"" + photoURL + "\"}";

        params.put("email", registrationEmail);
        params.put("password", registrationPassword);
        params.put("sessionExpiration", Integer.parseInt(exp));
        params.put("profile", profileJson);

        return params;
    }

    private GSObject parseLoginParameters(HttpServletRequest req) {
        final GSObject params = new GSObject();

        final String loginId = req.getParameter("loginId");
        final String loginPassword = req.getParameter("password");

        params.put("loginID", loginId);
        params.put("password", loginPassword);
        params.put("include", "profile,data,subscriptions,preferences,id_token");

        return params;
    }

    private GSObject parseGetAccountInfoParameters(HttpServletRequest req) {
        final GSObject params = new GSObject();

        final String uid = req.getParameter("UID");

        params.put("UID", uid);

        return params;
    }

    private GSObject parseVerifyJWTToken(HttpServletRequest req) {
        final GSObject params = new GSObject();

        final String uid = req.getParameter("id_token");

        params.put("id_token", uid);

        return params;
    }
}
