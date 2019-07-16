import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSRequest;
import com.gigya.socialize.GSResponse;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SimpleRequestDispatcher {

    static final private String API_KEY = "3_eP-lTMvtVwgjBCKCWPgYfeWH4xVkD5Rga15I7aoVvo-S_J5ZRBLg9jLDgJvDJZag";
    static final private String API_SECRET = "KtA79ljpc5ursCT7aXyFhnO8/AL2jUlTobBMY4wx4r0=";
    static final private String USER_KEY = "Key:ANiHp6OEaqFZ";

    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
        executorService.execute(() -> {
            login();
        });
    }

    private static GSRequest newGSRequest(String apiKey, String apiSecret, String apiMethod, String userKey) {
        GSObject params = new GSObject();
        params.put("format", "json");
        GSRequest request = new GSRequest(apiKey, apiSecret, apiMethod, params, true /* useHttps */, userKey);
        GSRequest.ENABLE_CONNECTION_POOLING = true;
        return request;
    }

    private static void register() {
        final GSRequest initRegistrationRequest = newGSRequest(API_KEY, API_SECRET, "accounts.initRegistration", null);
        final GSResponse initRegistrationResponse = initRegistrationRequest.send();
        if (initRegistrationResponse.getErrorCode() == 0) {

            final GSObject params = new GSObject();
            params.put("email", "toolmarmel.alt1123@gmail.com");
            params.put("password", "123123");
            params.put("sessionExpiration", 0);

            final String regToken = initRegistrationResponse.getString("regToken", null);
            if (regToken != null) {
                params.put("regToken", regToken);
                params.put("finalizeRegistration", true);
            }

            // Register request.
            final GSRequest registerRequest = newGSRequest(API_KEY, API_SECRET, "accounts.register", null);
            registerRequest.setParams(params);
            final GSResponse registerResponse = registerRequest.send();
            if (registerResponse.getErrorCode() == 0) {
                System.out.println(registerResponse.getData().toJsonString());
            } else {
                System.out.println(registerResponse.getResponseText());
            }
        } else {
            System.out.println(initRegistrationResponse.getResponseText());
        }
    }

    private static void getJWTPublicKey() {
        final GSObject params = new GSObject();
        params.put("V2", true);
        final GSRequest request = new GSRequest(API_KEY, API_SECRET, "accounts.getJWTPublicKey", params);
        GSResponse response = request.send();
        if (response.getErrorCode() == 0) {
            System.out.println(response.getData().toJsonString());
        } else {  // Error
            System.out.println("sendGetAccountInfoRequest: Error");
        }
    }

    private static void login() {
        GSRequest request = newGSRequest(API_KEY, API_SECRET, "accounts.login", null);
        request.setParam("loginID", "toolmarmel.alt1@gmail.com");
        request.setParam("password", "123123");
        request.setParam("include", "id_token");
        // Send request.
        GSResponse response = request.send();
        System.out.println("sendSearchRequest: sendGetAccountInfo:\n" + response.toString());
        if (response.getErrorCode() == 0) {
            System.out.println("sendGetAccountInfoRequest: Success");
        } else {  // Error
            System.out.println("sendGetAccountInfoRequest: Error");
        }
    }

    private static void getAccountInfo() {
        GSRequest request = newGSRequest(API_KEY, API_SECRET, "accounts.getAccountInfo", null);
        request.setParam("uid", "5721a1c011194a8b9c1b806e3c65f6ab"); // toolmarmel.alt1@gmail.com
        request.setParam("include", "id_token");
        // Send request.
        GSResponse response = request.send();
        System.out.println("sendSearchRequest: sendGetAccountInfo:\n" + response.toString());
        if (response.getErrorCode() == 0) {
            System.out.println("sendGetAccountInfoRequest: Success");
        } else {  // Error
            System.out.println("sendGetAccountInfoRequest: Error");
        }
    }

    private static void testJWTVerify() throws IOException {

        String https_url = "https://accounts.us1-st2.gigya.com/accounts.login";

        String urlParameters = "ApiKey=" + API_KEY + "&loginID=toolmarmel.alt1@gmail.com&password=123123&include=id_token";

        Map<String, Object> params = new HashMap<>();
        params.put("ApiKey", "3_okzFVIQTsXw5vS6s0y9BEm6T4fbNTPVox6DZAwn-rCC7ca1dv6LhrPCdksCiSfOc");
        params.put("loginID", "toolmarmel.alt1@gmail.com");
        params.put("password", "123123");
        params.put("include", "id_token");

        OutputStreamWriter outputStreamWriter = null;
        HttpsURLConnection con = null;
        try {
            URL url = new URL(https_url);
            con = (HttpsURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("connection", "close");
            con.setRequestMethod("POST");

            con.setDoOutput(true);
            outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
            outputStreamWriter.write(buildEncodedQuery(params));
            outputStreamWriter.flush();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }


    }

    public static String buildEncodedQuery(Map<String, Object> params) {
        if (params.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> item : params.entrySet()) {
            final Object value = item.getValue();
            final String key = item.getKey();
            if (value != null) {
                sb.append(key);
                sb.append('=');
                sb.append(urlEncode(String.valueOf(item.getValue())));
                sb.append('&');
            }
        }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8").replace("+", "%20")
                    .replace("*", "%2A").replace("%7E", "~");
        } catch (Exception ex) {
            return null;
        }
    }
}
