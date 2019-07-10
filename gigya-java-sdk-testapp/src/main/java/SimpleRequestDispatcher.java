import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSRequest;
import com.gigya.socialize.GSResponse;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SimpleRequestDispatcher {

    static final private String API_KEY = "3_eP-lTMvtVwgjBCKCWPgYfeWH4xVkD5Rga15I7aoVvo-S_J5ZRBLg9jLDgJvDJZag";
    static final private String API_SECRET = "KtA79ljpc5ursCT7aXyFhnO8/AL2jUlTobBMY4wx4r0=";
    static final private String USER_KEY = "";

    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
        executorService.execute(SimpleRequestDispatcher::getAccountInfo);
    }

    private static GSRequest newGSRequest(String apiKey, String apiSecret, String apiMethod, String userKey) {
        GSObject params = new GSObject();
        params.put("format", "json");
        GSRequest request = new GSRequest(apiKey, apiSecret, apiMethod, params, true /* useHttps */, userKey);
        GSRequest.ENABLE_CONNECTION_POOLING = true;
        return request;
    }

    private static void getJWTPublicKey()  {
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

    private static void getAccountInfo() {
        GSRequest request = newGSRequest(API_KEY, API_SECRET, "accounts.getAccountInfo", null);
        request.setParam("uid", "cbb9ed29b97d47bbb12a3e08acb6df90"); // toolmarmel.alt1@gmail.com
        // Send request.
        GSResponse response = request.send(60000);
        System.out.println("sendSearchRequest: sendGetAccountInfo:\n" + response.toString());
        if (response.getErrorCode() == 0) {
            System.out.println("sendGetAccountInfoRequest: Success");
        } else {  // Error
            System.out.println("sendGetAccountInfoRequest: Error");
        }
    }
}
