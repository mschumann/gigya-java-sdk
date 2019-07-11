import com.gigya.auth.GSAuthRequest;
import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSRequest;
import com.gigya.socialize.GSResponse;

import javax.servlet.ServletContext;

class RequestDispatcher implements IRequestDispatcher {

    final private boolean isGlobal;

    RequestDispatcher(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    private ServletContext _context;

    void setContext(ServletContext context) {
       _context = context;
    }

    //region APIS

    private static final String API_INIT_REGISTRATION = "accounts.initRegistration";
    private static final String API_REGISTER = "accounts.register";
    private static final String API_LOGIN = "accounts.login";
    private static final String API_GET_ACCOUNT_INFO = "accounts.getAccountInfo";
    private static final String API_VERIFY_LOGIN = "accounts.verifyLogin";
    private static final String API_IS_AVAILABLE_LOGIN_ID = "accounts.isAvailableLoginID";
    private static final String API_GET_JWT_PUBLIC_KEY = "accounts.getJWTPublicKey";

    private GSRequest getRequest(String method) {
        return getRequest(method, null);
    }

    private GSRequest getRequest(String method, GSObject params) {
        if (this.isGlobal) {
            GSAuthRequest request = new GSAuthRequest(Defines.ACCOUNT_USER_KEY, Defines.PRIVATE_KEY_GLOBAL, method, params);
            request.setAPIDomain(Defines.API_DOMAIN_GLOBAL);
            return request;
        } else {
            GSRequest request = new GSRequest(Defines.API_KEY_TEST, Defines.API_SECRET_TEST, method, params);
            request.setAPIDomain(Defines.API_DOMAIN);
            return request;
        }
    }

    @Override
    public String register(String email, String password, int exp, String profile) {

        final GSRequest initRegistrationRequest = getRequest(API_INIT_REGISTRATION, null);
        _context.log("register: initRequest = " + initRegistrationRequest.getMethod());

        final GSResponse initRegistrationResponse = initRegistrationRequest.send();
        if (initRegistrationResponse.getErrorCode() == 0) {

            final GSObject params = new GSObject();
            params.put("email", email);
            params.put("password", password);
            params.put("sessionExpiration", exp);
            params.put("profile", profile);

            final String regToken = initRegistrationResponse.getString("regToken", null);
            if (regToken != null) {
                params.put("regToken", regToken);
                params.put("finalizeRegistration", true);
            }

            // Register request.
            final GSRequest registerRequest = getRequest(API_REGISTER, params);
            final GSResponse registerResponse = registerRequest.send();
            if (registerResponse.getErrorCode() == 0) {
                return registerResponse.getData().toJsonString();
            } else {
                return registerResponse.getResponseText();
            }
        } else {
            return initRegistrationResponse.getResponseText();
        }
    }

    @Override
    public String login(String loginId, String password) {
        final GSObject params = new GSObject();
        params.put("loginID", loginId);
        params.put("password", password);
        params.put("include", "profile,data,subscriptions,preferences");

        final GSRequest request = getRequest(API_LOGIN, params);
        final GSResponse response = request.send();
        if (response.getErrorCode() == 0) {
            return response.getData().toJsonString();
        } else {
            return response.getResponseText();
        }
    }

    @Override
    public String getAccountInfo(String uid) {
        final GSRequest request = getRequest(API_GET_ACCOUNT_INFO);
        request.setParam("uid", uid); // toolmarmel.alt1@gmail.com
        final GSResponse response = request.send();
        if (response.getErrorCode() == 0) {
            return response.getData().toJsonString();
        } else {  // Error
            return response.getResponseText();
        }
    }

    @Override
    public String verifyLogin(String uid) {
        final GSRequest request = getRequest(API_VERIFY_LOGIN);
        request.setParam("UID", uid);
        request.setParam("include", "identities-all,loginIDs,profile,email,data");
        final GSResponse response = request.send();
        if (response.getErrorCode() == 0) {
            return response.getData().toJsonString();
        } else {
            return response.getResponseText();
        }
    }

    @Override
    public String isAvailableLoginId(String loginId) {
        final GSRequest request = getRequest(API_IS_AVAILABLE_LOGIN_ID);
        request.setParam("loginID", loginId);
        final GSResponse response = request.send();
        if (response.getErrorCode() == 0) {
            return response.getData().toJsonString();
        } else {
            return response.getResponseText();
        }
    }

    @Override
    public String getJWTPublicKey() {
        final GSRequest request = getRequest(API_GET_JWT_PUBLIC_KEY);
        request.setParam("V2", true);
        final GSResponse response = request.send();
        if (response.getErrorCode() == 0) {
            return response.getData().toJsonString();
        } else {  // Error
            return response.getResponseText();
        }
    }

    //endregion
}
