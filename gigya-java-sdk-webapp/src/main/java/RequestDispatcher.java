import com.gigya.auth.GSAuthRequest;
import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSRequest;
import com.gigya.socialize.GSResponse;

class RequestDispatcher {

    final private boolean useAuthenticatedRequest;

    RequestDispatcher(boolean useAuthenticatedRequest) {
        this.useAuthenticatedRequest = useAuthenticatedRequest;
    }

    //region APIS

    private static final String API_INIT_REGISTRATION = "accounts.initRegistration";
    private static final String API_REGISTER = "accounts.register";
    private static final String API_LOGIN = "accounts.login";
    private static final String API_GET_ACCOUNT_INFO = "accounts.getAccountInfo";
    private static final String API_GET_JWT_PUBLIC_KEY = "accounts.getJWTPublicKey";

    private GSRequest getRequest(String method) {
        return getRequest(method, null);
    }

    private GSRequest getRequest(String method, GSObject params) {
        if (this.useAuthenticatedRequest) {
            GSAuthRequest request = new GSAuthRequest(Defines.ACCOUNT_USER_KEY, Defines.PRIVATE_KEY, method);
            request.setParams(params);
            request.setAPIDomain(Defines.API_DOMAIN);
            return request;
        } else {
            GSRequest request = new GSRequest(Defines.API_KEY, Defines.API_SECRET, method, params);
            request.setAPIDomain(Defines.API_DOMAIN);
            return request;
        }
    }

    String register(final GSObject params) {
        final GSRequest initRegistrationRequest = getRequest(API_INIT_REGISTRATION, null);
        final GSResponse initRegistrationResponse = initRegistrationRequest.send();
        if (initRegistrationResponse.getErrorCode() == 0) {
            final String regToken = initRegistrationResponse.getString("regToken", null);
            if (regToken != null) {
                params.put("regToken", regToken);
                params.put("finalizeRegistration", true);
                params.put("include", "id_token");
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

    String login(final GSObject params) {
        final GSRequest request = getRequest(API_LOGIN, params);
        final GSResponse response = request.send();
        if (response.getErrorCode() == 0) {
            return response.getData().toJsonString();
        } else {
            return response.getResponseText();
        }
    }

    String getAccountInfo(final GSObject params) {
        final GSRequest request = getRequest(API_GET_ACCOUNT_INFO);
        request.setParams(params);
        final GSResponse response = request.send();
        if (response.getErrorCode() == 0) {
            return response.getData().toJsonString();
        } else {  // Error
            return response.getResponseText();
        }
    }

    String getJWTPublicKey() {
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
