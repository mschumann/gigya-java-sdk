package com.gigya.auth;

import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSRequest;
import com.gigya.socialize.GSResponse;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.security.InvalidKeyException;

public class GSAuthRequest extends GSRequest {

    private String privateKey;

    /**
     * @param userKey    Account user key
     * @param privateKey Account private key
     * @param apiMethod  Request api method
     * @param apiKey     Site api key
     */
    public GSAuthRequest(String userKey, String privateKey, String apiKey,  String apiMethod) {
        super(apiKey, null, null, apiMethod, null, true, userKey);
        this.privateKey = privateKey;

    }

    @Override
    protected void sign(String token, String secret, String httpMethod, String resourceURI)
            throws UnsupportedEncodingException, InvalidKeyException, MalformedURLException {
        // Compose jwt && add to request header.
        final String jwt = GSAuthRequestUtils.composeJwt(userKey, privateKey);
        if (jwt == null) {
            logger.write("Failed to generate authorization JWT");
        }
        addHeader("Authorization", "Bearer " + jwt);
    }

    @Override
    public GSResponse send() {
        return send(-1);
    }

    @Override
    public GSResponse send(int timeoutMS) {

        if (apiKey != null) {
            setParam("apiKey",this.apiKey);
        }

        if (this.apiMethod.startsWith("/"))
            this.apiMethod = this.apiMethod.replaceFirst("/", "");

        //noinspection IndexOfReplaceableByContains
        if (this.apiMethod.indexOf(".") == -1) {
            this.host = "socialize." + apiDomain;
            this.path = "/socialize." + apiMethod;
        } else {
            String[] tokens = apiMethod.split("\\.");
            this.host = tokens[0] + "." + apiDomain;
            this.path = "/" + apiMethod;
        }

        // use "_host" to override domain, if available
        this.host = this.params.getString("_host", this.host);
        this.params.remove("_host");

        this.format = this.params.getString("format", "json");
        setParam("format", this.format);

        logger.write("apiKey", apiKey);
        logger.write("userKey", userKey);
        logger.write("apiMethod", apiMethod);
        logger.write("params", params);
        logger.write("useHTTPS", useHTTPS);

        try {
            GSResponse res = sendRequest("POST", this.host, this.path,
                    params, apiKey, secretKey, this.useHTTPS, this.isLoggedIn,
                    timeoutMS);
            // if error code indicates timestamp expiration, retry the request.
            // (sendRequest calculates the tsOffset)
            if (res.getErrorCode() == 403002 && !isRetry) {
                isRetry = true;
                params.remove("sig");
                return send();
            } else {
                return res;
            }
        } catch (InvalidKeyException exKey) {
            return new GSResponse(this.apiMethod, this.params, 400006,
                    "Invalid parameter value:" + exKey.getMessage(), logger);
        } catch (UnsupportedEncodingException | IllegalArgumentException exEncoding) {
            return new GSResponse(this.apiMethod, this.params, 400006,
                    "Invalid parameter value: " + exEncoding.getMessage(),
                    logger);
        } catch (SocketTimeoutException exTimeout) {
            return new GSResponse(this.apiMethod, this.params, 504002,
                    "Request Timeout", logger);
        } catch (Exception ex) {
            logger.write(ex);
            return new GSResponse(this.apiMethod, this.params, 500000,
                    ex.toString(), logger);
        }
    }
}
