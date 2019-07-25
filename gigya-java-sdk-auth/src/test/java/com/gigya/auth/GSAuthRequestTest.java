package com.gigya.auth;

import com.gigya.socialize.GSResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(PowerMockRunner.class)
public class GSAuthRequestTest {

    @Test
    public void test_authorizationRequest() {

        // Arrange
        final String USER_KEY = "ANiHp6OEaqFZ";
        final String API_KEY = "3_okzFVIQTsXw5vS6s0y9BEm6T4fbNTPVox6DZAwn-rCC7ca1dv6LhrPCdksCiSfOc";
        final String PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\\r\\nMIIEowIBAAKCAQEArRdCJQhIRLCD0WChqSvuk+DrzSV512r00tv0gznjnNWpOsuy\\r\\nCZuD3D6qyOi27QRDqfEZuPK+58NvprNKWF2Qwe2OSkWH+VgW9AhyRj0g7NH8v2T7\\r\\nJwtFN/PB6fobTjJYC1QmFBKnGZB+ZacsYZGMd1XcS2NQByxp1gN9WP5kQ1pvC1Nv\\r\\nQmVmLyoBpJKTE5ab6weCDWM0XyRBeuge7JhT8BUvDmv4YrMU+ad018Sxqo646atG\\r\\neE7b3d1mM3oIrl5ev0qIgb1DYH4Zp1PPGqWMDWM9A1Y/gZQW969xwSCvzXlvMyjv\\r\\nX/IuahRRoZXUZEBIlprY3GYi0FgsRl3w3K2tyQIDAQABAoIBAD7RUJORCG1JZGWR\\r\\n/MmhJIItoFZA8hGggv6djgUr9rOeBT9N2qzK8FV3ttKYRn8JS6c61Xt+OZNyJayp\\r\\np+Me/eCrnrbMujbfZeElyZc9KxxXbzNk6hM272WJhQgt2UgNVE+0N+eUB2nrOzwY\\r\\nKi60vHY4xmzju5EG6NgfRxgLpwacC2zg0xzjXDJt9EKPMd0sEaxTVeRBAbM+r1Ad\\r\\nwV07fDTLpQDlC/P2Z0oFYS/ri6l5yyJAOgGjiOEEWF+WTThehhgpSdHTGPCMkOPV\\r\\n3fsMfI1E6jW2M1LENr06vi9x39KgH+O3GUjFq6/Wn8D9tPVnw57Z3tsHIGAx/4cO\\r\\n9+npN6sCgYEA59MFpQMAkQW8CR/YP8ZcFZumWnkrYu1osZr7PIQv37DQTYDpXn6w\\r\\nXn5H4osBfLll/j+9KlLGZi6AFhbSVXUdNk2k4i7b71FXcgyBZ5mi8vJKv72ws2mw\\r\\n1i1c4KUTfBkneF5sOsEv1TfcCNlM4V44mzM6CYddZq/ehUPVG2vvVmcCgYEAvyQ9\\r\\nBVjApe9HdoZAVxjgZ7Sq5ah5xrXl4JoIdHBwZ0j8DELcVMTBZ1nSf/jayG/VP2sC\\r\\nYT+fdJCo5ON3vEd1n8e4+FVmUod6uOLmqbA+y9NAO6x21CYOpTOt7T80MzAPqBle\\r\\nmLZXkkjg8GSO00VEdnu5kTv88ziFlMP2R+HzXE8CgYAkFJvUSttTtujFIsetZVn/\\r\\novIJMsKl6v1YZx6jFI++6O4CogWwR1d73Xuirq/UkaZmI9V1ONzYGnSk+3hRBKE/\\r\\nSmZXLaPdY3OQtJGPZkIOPdeUcBmIUvLK4tSJwid+MQwLl+aQXnDrZn5AglGM4fU9\\r\\ncoOd6AuJZ+XiJ+SLGAlwEQKBgQCkTzGJdBb/Tab/8z5B4OefHoa+L0qnj9wRbDgS\\r\\naEmn7+yDWcJvuEeNYYB7c+AW51PIVWPRV1DqciRNxmA6YK9zb8vI5hi0nzPW1W51\\r\\ntkT1EB8qbQIlMqXqS3ea93cW/Zyn408XNcAnxzBeVgMVCHlm5MrLV2cFabQjAyWl\\r\\n+00pLwKBgGqIUgo3xTVzu5ji5kc7wk3LIdIeb6vMItHxHEMjIFBj28jAW7I0Dglg\\r\\nYdloUCevRwtgpyNTo4Th4kXdjpeTmF/CL/NHVeYxurtToYc1tzqEuuntzNYG9+0a\\r\\nPQ8UaIzwJJeU/UQU5U+rSwNU//JIwbzCy7J0kU6u6pqr3USGW+wT\\r\\n-----END RSA PRIVATE KEY-----\\r\\n";

        // Act
        final GSAuthRequest request = new GSAuthRequest(USER_KEY, PRIVATE_KEY, API_KEY, "accounts.getAccountInfo");
        request.setParam("UID", "cae35bb4103a4466b81fcefd6f5f6721");
        request.setAPIDomain("us1-st2.gigya.com");
        final GSResponse response = request.send();

        System.out.println(response.getResponseText());

        // Assert
        assertEquals(0, response.getErrorCode());
    }

    @Test
    public void test_verifyIdToken() {

        // Arrange
        final String API_KEY = "3_eP-lTMvtVwgjBCKCWPgYfeWH4xVkD5Rga15I7aoVvo-S_J5ZRBLg9jLDgJvDJZag";
        // Need to update the id token to a valid one prior to test.
        final String idToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IlJFUTBNVVE1TjBOQ1JUSkVNemszTTBVMVJrTkRRMFUwUTBNMVJFRkJSamhETWpkRU5VRkJRZyIsImRjIjoidXMxIn0.eyJpc3MiOiJodHRwczovL2ZpZG0uZ2lneWEuY29tL2p3dC8zX2VQLWxUTXZ0VndnakJDS0NXUGdZZmVXSDR4VmtENVJnYTE1STdhb1Z2by1TX0o1WlJCTGc5akxEZ0p2REpaYWciLCJhcGlLZXkiOiIzX2VQLWxUTXZ0VndnakJDS0NXUGdZZmVXSDR4VmtENVJnYTE1STdhb1Z2by1TX0o1WlJCTGc5akxEZ0p2REpaYWciLCJzdWIiOiI1NzIxYTFjMDExMTk0YThiOWMxYjgwNmUzYzY1ZjZhYiIsImlhdCI6MTU2NDA1NTQ5MCwiZXhwIjoxNTY0MDU1NTUwLCJpc0xvZ2dlZEluIjoiRmFsc2UifQ.CNdIy45AytPW5z27yLDvzbDmOgrt7ShRoxgnF7CbLl-bUx0dhFOWzQXb9I6uLNnDsPkYUU5wGb6yl_2Xo0gZPl692cXg2nBuRqLBxY59EcjByQVYTwRpATXotFMwgQ0wkA2VEvTCP3ppHNTet3dZhKmhpd-Z6CUi_Pd_caRyBLOChRzC2Y2LxJ6tI44P2drrMJqJ4ZRr0jzlMhhRLAEPkj5p-nKzldBXn1xpFVJjFZdtwjUICNoEmCCh1iLqTjxAv_Ao2zVhNWg_7pOTL1K70JZ5kLWhgpzQVJ31hJO_sNHNWRTE1yA7nNJV-6VjIb28hdZw8ZWDLiwuZTGG5M9ENQ";
        final String jwk = "{\n\"alg\": \"RS256\",\n\"kid\": \"REQ0MUQ5N0NCRTJEMzk3M0U1RkNDQ0U0Q0M1REFBRjhDMjdENUFBQg\",\n\"kty\": \"RSA\",\n\"n\": \"qoQah4MFGYedrbWwFc3UkC1hpZlnB2_E922yRJfHqpq2tTHL_NvjYmssVdJBgSKi36cptKqUJ0Phui9Z_kk8zMPrPfV16h0ZfBzKsvIy6_d7cWnn163BMz46kAHtZXqXhNuj19IZRCDfNoqVVxxCIYvbsgInbzZM82CB86iYPAS7piijYn1S6hueVHGAzQorOetZevKIAvbH3kJXZ4KdY6Ffz5SFDJBxC3bycN4q2JM1qnyD53vcc0MitxyIUF7a06iJb5_xXBiA-3xnTI0FU5hw_k6x-sdB5Rglx13_2aNzdWBSBAnxs1XXtZUt9_2RAUxP1XORkrBGlPg9D7cBtQ\",\n\"e\": \"AQAB\",\n\"use\": \"sig\"\n}";

        // Act
        GSAuthRequestUtils.addToPublicKeyCache("us1.gigya.com", jwk);
        final String UID = GSAuthRequestUtils.validateSignature(idToken, API_KEY, "us1.gigya.com");

        // Assert
        assertEquals("5721a1c011194a8b9c1b806e3c65f6ab", UID);
    }

    @Test
    public void test_validateSignature_with_expired_idToken() {

        // Arrange
        final String API_KEY = "3_eP-lTMvtVwgjBCKCWPgYfeWH4xVkD5Rga15I7aoVvo-S_J5ZRBLg9jLDgJvDJZag";
        final String expiredIdToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IlJFUTBNVVE1TjBOQ1JUSkVNemszTTBVMVJrTkRRMFUwUTBNMVJFRkJSamhETWpkRU5VRkJRZyIsImRjIjoidXMxIn0.eyJpc3MiOiJodHRwczovL2ZpZG0uZ2lneWEuY29tL2p3dC8zX2VQLWxUTXZ0VndnakJDS0NXUGdZZmVXSDR4VmtENVJnYTE1STdhb1Z2by1TX0o1WlJCTGc5akxEZ0p2REpaYWciLCJhcGlLZXkiOiIzX2VQLWxUTXZ0VndnakJDS0NXUGdZZmVXSDR4VmtENVJnYTE1STdhb1Z2by1TX0o1WlJCTGc5akxEZ0p2REpaYWciLCJzdWIiOiIzN2YwYmM0ZDQ5MGI0YjhkOTRhYmZiNDgxMDMyZGMwZiIsImlhdCI6MTU2MzI3MTAzNiwiZXhwIjoxNTYzMjcxMDk2LCJhdXRoX3RpbWUiOjE1NjMyNzEwMzZ9.gx5CrOutre1TwhOFwEulgE7htUHoXAUKa1lnW5nJq5fKPzcaSCpV0y0OjtkgCKQBo0v7P_cT1BTs4yw2Eef0xC9yDW5VBFteB77_Ukxqu2Q9lwYAuLXRQKz1ALjPfAY25b6UhLIpfOt2Zpxfgqj8LZuBLYRQpa6digbeTwD3oBCIEfrbjypmGTD0y_LkDjihyYb3Y5Yvoa-x1mfufCYPNBBNIi8kmPPPiVVXznrFdQlLC7-eVVsVd0dDKvhnCllb2Csrmr4qhVZx6c7tnGsp1ZBlY2CXasTR1Uu6_oG8W9aJdYF4zTeKwXEoUGXLJupn_UVuU-RTXKtRjgKVSspXqQ";
        final String jwk = "{\n\"alg\": \"RS256\",\n\"kid\": \"REQ0MUQ5N0NCRTJEMzk3M0U1RkNDQ0U0Q0M1REFBRjhDMjdENUFBQg\",\n\"kty\": \"RSA\",\n\"n\": \"qoQah4MFGYedrbWwFc3UkC1hpZlnB2_E922yRJfHqpq2tTHL_NvjYmssVdJBgSKi36cptKqUJ0Phui9Z_kk8zMPrPfV16h0ZfBzKsvIy6_d7cWnn163BMz46kAHtZXqXhNuj19IZRCDfNoqVVxxCIYvbsgInbzZM82CB86iYPAS7piijYn1S6hueVHGAzQorOetZevKIAvbH3kJXZ4KdY6Ffz5SFDJBxC3bycN4q2JM1qnyD53vcc0MitxyIUF7a06iJb5_xXBiA-3xnTI0FU5hw_k6x-sdB5Rglx13_2aNzdWBSBAnxs1XXtZUt9_2RAUxP1XORkrBGlPg9D7cBtQ\",\n\"e\": \"AQAB\",\n\"use\": \"sig\"\n}";

        // Act
        GSAuthRequestUtils.addToPublicKeyCache("us1.gigya.com", jwk);
        final String UID = GSAuthRequestUtils.validateSignature(expiredIdToken, API_KEY, "us1.gigya.com");

        // Assert
        assertNull(UID);
    }


}
