import com.gigya.auth.GSAuthRequest;
import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSRequest;
import com.gigya.socialize.GSResponse;

class RequestDispatcher implements IRequestDispatcher {

    final private boolean isGlobal;

    RequestDispatcher(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    //region APIS

    private GSRequest getRequest(String method) {
        return getRequest(method, null);
    }

    private GSRequest getRequest(String method, GSObject params) {
        if (this.isGlobal) {
            GSAuthRequest request = new GSAuthRequest(
                    Defines.ACCOUNT_USER_KEY,
                    Defines.PRIVATE_KEY_GLOBAL,
                    method,
                    Defines.API_KEY_TEST_GLOBAL,
                    params);
            request.setAPIDomain(Defines.API_DOMAIN_GLOBAL);
            return request;
        } else {
            GSRequest request = new GSRequest(
                    Defines.API_KEY_TEST,
                    Defines.API_SECRET_TEST,
                    method,
                    params);
            request.setAPIDomain(Defines.API_DOMAIN);
            return request;
        }
    }

    @Override
    public String getPublicKey() {
        final GSRequest request = getRequest("accounts.getJWTPublicKey");
        request.setParam("V2", true);
        final GSResponse response = request.send();
        if (response.getErrorCode() == 0) {
            return response.getData().toJsonString();
        } else {  // Error
            return response.getResponseText();
        }
    }

    @Override
    public String getAccountInfo(String uid) {
        final GSRequest request = getRequest("accounts.getAccountInfo");
        request.setParam("uid", uid); // toolmarmel.alt1@gmail.com
        final GSResponse response = request.send();
        if (response.getErrorCode() == 0) {
            return response.getData().toJsonString();
        } else {  // Error
            return response.getResponseText();
        }
    }

    @Override
    public String register(String email, String password, int exp) {

        final GSRequest initRegistrationRequest = getRequest("accounts.initRegistration", null);
        final GSResponse initRegistrationResponse = initRegistrationRequest.send();
        if (initRegistrationResponse.getErrorCode() == 0) {

            final GSObject params = new GSObject();
            params.put("email", email);
            params.put("password", password);
            params.put("sessionExpiration", exp);

            final String regToken = initRegistrationResponse.getString("regToken", null);
            if (regToken != null) {
                params.put("regToken", regToken);
                params.put("finalizeRegistration", true);
            }

            // Register request.
            final GSRequest registerRequest = getRequest("accounts.register", params);
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

        final GSRequest request = getRequest("accounts.login", params);
        final GSResponse response = request.send();
        if (response.getErrorCode() == 0) {
            return response.getData().toJsonString();
        } else {
            return response.getResponseText();
        }
    }

    //endregion
}
