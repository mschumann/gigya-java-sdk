package com.gigya.auth;

import com.gigya.socialize.GSArray;
import com.gigya.socialize.GSResponse;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class GSAnonymousRequestTest {

    @Test
    public void test_getJWTPublicKey()  {

        final GSAnonymousRequest request = new GSAnonymousRequest(
                "3_okzFVIQTsXw5vS6s0y9BEm6T4fbNTPVox6DZAwn-rCC7ca1dv6LhrPCdksCiSfOc",
                "us1-st2.gigya.com",
                "accounts.getJWTPublicKey");
        request.setParam("V2", true);
        final GSResponse response = request.send();

        assertEquals(0, response.getErrorCode());

        System.out.println(response.getResponseText());

        final GSArray keyArray = response.getArray("keys", null);
        assertNotNull(keyArray);

        final String key = keyArray.get(0).toString();
        assertNotNull(key);

        System.out.println(key);
    }
}
