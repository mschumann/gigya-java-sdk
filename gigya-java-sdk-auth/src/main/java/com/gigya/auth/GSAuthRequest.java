package com.gigya.auth;

import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSRequest;
import com.gigya.socialize.GSResponse;

public class GSAuthRequest extends GSRequest {

    private String privateKey;

    /**
     * @param userKey    Account user key
     * @param privateKey Account private key
     * @param apiMethod  Request api method
     */
    public GSAuthRequest(String userKey, String privateKey, String apiMethod) {
        super(null, null, null, apiMethod, null, true, userKey);
        this.privateKey = privateKey;
    }

    /**
     * @param userKey    Account user key
     * @param privateKey Account private key
     * @param apiMethod  Request api method
     * @param apiKey     Site api key
     */
    public GSAuthRequest(String userKey, String privateKey, String apiMethod, String apiKey) {
        super(apiKey, null, null, apiMethod, null, true, userKey);
        this.privateKey = privateKey;
    }
    /**
     * @param userKey      Account user key
     * @param privateKey   Account private key
     * @param apiMethod    Request api method
     * @param apiKey       Site api key
     * @param clientParams Request parameters
     */
    public GSAuthRequest(String userKey, String privateKey, String apiMethod, String apiKey, GSObject clientParams) {
        super(apiKey, null, null, apiMethod, clientParams, true, userKey);
        this.privateKey = privateKey;
    }

    @Override
    public GSResponse send() {
        // Compose jwt && add to request header.
        final String jwt = GSAuthRequestUtils.composeJwt(userKey, privateKey);
        if (jwt != null) {
            addHeader("Authorization", "Bearer " + jwt);
        }
        return super.send();
    }
}
