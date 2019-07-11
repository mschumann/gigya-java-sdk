import com.gigya.auth.GSAuthRequestUtils;
import com.gigya.socialize.GSObject;

class BloC {

    boolean verifyJWTToken(final GSObject params) {

        final String jwt = params.getString("id_token", "");
        return GSAuthRequestUtils.validateGigyaSignature(jwt, Defines.API_KEY, Defines.API_SECRET);
    }
}
