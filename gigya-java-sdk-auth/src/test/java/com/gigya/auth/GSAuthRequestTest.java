package com.gigya.auth;

import com.gigya.socialize.GSResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.PublicKey;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
public class GSAuthRequestTest {

    @Test
    public void test_authorizationRequest() {

        // Arrange
        final String USER_KEY = "ANiHp6OEaqFZ";
        final String API_KEY = "3_okzFVIQTsXw5vS6s0y9BEm6T4fbNTPVox6DZAwn-rCC7ca1dv6LhrPCdksCiSfOc";
        final String PRIVATE_KEY = "\\r\\nMIIEpAIBAAKCAQEAzT07aJ2xKgo9x2ci8yJwg63h3m7aK19+f8XL1GGu9hiJKlhP\\r\\nCeG9HJAaYpE2RTKbjWQ+NZSG9H47/tIRvrv5I7K5Ciaoe9/nLGnaL11Pr6xPD0x3\\r\\nf93TYTcATJd6QT80xdttviWj0jBxZsmidGQZZbIxjLNQteKLIv4UblJTEJMBz87l\\r\\nzuqENA0qK5JgWl2fX6DDOBFaauE9eThbXPmwgUenMKtMDm9XpJh7WehLx6IGm2hh\\r\\nZ3bO2ts05BeHk73nqB4VZaa8zjSZLmxHL6UxtcHuTRxno/fsHONDkEaFvhlw+Fvc\\r\\ngZ9qRv0qRJKP3mV6cB0aCzaSWSVOxn/4ptmnJwIDAQABAoIBADkUpZgz+1j43jO+\\r\\nIH9EfcxxqNcHN5BGj9UTetwHivSQ4F0xkFHsQX9XKK9vOYvHRnHthku6kkofbaoy\\r\\nXJMSjnj6NPJ4y/Uf0ZiNnAv+RLJUFuznVHjwheCMvn2Ox4asAI5it8Phjg1gQbnX\\r\\nrGH4kQOyqos6N+FOL2o9Bgukw8tgxyZ/aIRkwhrar04vICEw+7FYp/vIula6UvFX\\r\\nPGY5U1/XNiHL531dVG1idsGWF8diAx8LPRga03p29bDRqjuTKBQS8LBdNU/zPD50\\r\\nR2CCBiFrOl5KuD+0dzSF5bopgZZCY6fvxmkyPbboHmJt8S99S/ZnLl0S+k0FWTfA\\r\\nXl8APAkCgYEAzYimRs++YHy6x6ScSp0NAt2ZheXX0qofC0IUw+TG7t9pFTguJZSs\\r\\nAMkbnHC6UPimY1aMWfBUM2T/4zP4+8pSZxZ/FmyN3vWEcTIN7oHLUj2fzqxGbG4I\\r\\nmKJKNVeXYR9muUIKRFgBt++d25Nkl2YSwcIlY17jf2njmyQRyTUs5CMCgYEA/6IQ\\r\\nk17GBk+k082A9VxxuzhM+AQ46RtwP2T3NntOXFKazZBbphJSzIDM09e1qg572S/0\\r\\nkGLehcGxr+8Ftq1aNBqlERnaDCMJALS+yExqIpzXltX0BCMstFWEcqg2hvskrkTw\\r\\nzgMJ/FCTqdZL0mM/q37UjER0yn1wqkdwIsjCjy0CgYBEoA20swSDvuG2axXsK8f+\\r\\nXvM1q+qF4lt/LM82IRrzAxFZDmsKDTvo/z0C3Pi4tG0J2gn+tanHhz+RIvdNSt58\\r\\noRxJ5nDwMtXH3cidDCVRDlZfcVIKPisMm5NfgCGBQjTQQmafLytTuOPiPrJlgqVc\\r\\noHBPX41iK37dfAucPO8CNQKBgQDfMB0uvclBC1pKfhsAnhTCNBanV/BkPcuT3dAl\\r\\nJiU1X/2+SjJqXYci+7VZo/T9I6Yn6sGpKllECOfdRmADrXibE0RZGOwSOqiKkEQd\\r\\n1vG3hJ6KGATzc859KEj6Xjk1QqI08QYZp/eY7rTtzKboajTVw2dJjB84B6tNUA/s\\r\\nnsGXhQKBgQCfA9bkCUzTn+KSb/NBJRq9Lc32HHbtrHMOD4w/bNUPVfAaGY1oK0+b\\r\\nTzJUqo/EHieH9uU6fgy9Tqd5RHBJAEBPNiwXKFyRumel9unqxs1TzQ1EA9m3EF6/\\r\\n3ebD3YWGiMcHoKAx7MfU+imP+A2UMv9PIEVxfC0M1XwUcrsbNhJNwg==\\r\\n";

        // Act
        final GSAuthRequest request = new GSAuthRequest(USER_KEY, PRIVATE_KEY, API_KEY, "accounts.getAccountInfo");
        request.setParam("UID", "cae35bb4103a4466b81fcefd6f5f6721");
        request.setParam("include", "id_token");
        request.setAPIDomain("us1-st2.gigya.com");
        final GSResponse response = request.send();

        System.out.println(response.getResponseText());

        // Assert
        //assertEquals(0, response.getErrorCode());
    }

    @Test
    public void test_verifyIdToken() {

        final String API_KEY = "3_eP-lTMvtVwgjBCKCWPgYfeWH4xVkD5Rga15I7aoVvo-S_J5ZRBLg9jLDgJvDJZag";

        // Arrange
        final String id_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IlJFUTBNVVE1TjBOQ1JUSkVNemszTTBVMVJrTkRRMFUwUTBNMVJFRkJSamhETWpkRU5VRkJRZyIsImRjIjoidXMxIn0.eyJpc3MiOiJodHRwczovL2ZpZG0uZ2lneWEuY29tL2p3dC8zX2VQLWxUTXZ0VndnakJDS0NXUGdZZmVXSDR4VmtENVJnYTE1STdhb1Z2by1TX0o1WlJCTGc5akxEZ0p2REpaYWciLCJhcGlLZXkiOiIzX2VQLWxUTXZ0VndnakJDS0NXUGdZZmVXSDR4VmtENVJnYTE1STdhb1Z2by1TX0o1WlJCTGc5akxEZ0p2REpaYWciLCJzdWIiOiIzN2YwYmM0ZDQ5MGI0YjhkOTRhYmZiNDgxMDMyZGMwZiIsImlhdCI6MTU2MzI3MTAzNiwiZXhwIjoxNTYzMjcxMDk2LCJhdXRoX3RpbWUiOjE1NjMyNzEwMzZ9.gx5CrOutre1TwhOFwEulgE7htUHoXAUKa1lnW5nJq5fKPzcaSCpV0y0OjtkgCKQBo0v7P_cT1BTs4yw2Eef0xC9yDW5VBFteB77_Ukxqu2Q9lwYAuLXRQKz1ALjPfAY25b6UhLIpfOt2Zpxfgqj8LZuBLYRQpa6digbeTwD3oBCIEfrbjypmGTD0y_LkDjihyYb3Y5Yvoa-x1mfufCYPNBBNIi8kmPPPiVVXznrFdQlLC7-eVVsVd0dDKvhnCllb2Csrmr4qhVZx6c7tnGsp1ZBlY2CXasTR1Uu6_oG8W9aJdYF4zTeKwXEoUGXLJupn_UVuU-RTXKtRjgKVSspXqQ";
        System.out.println(id_token);
        final String jwk = "{\n" +
                "      \"alg\": \"RS256\",\n" +
                "      \"kid\": \"REQ0MUQ5N0NCRTJEMzk3M0U1RkNDQ0U0Q0M1REFBRjhDMjdENUFBQg\",\n" +
                "      \"kty\": \"RSA\",\n" +
                "      \"n\": \"qoQah4MFGYedrbWwFc3UkC1hpZlnB2_E922yRJfHqpq2tTHL_NvjYmssVdJBgSKi36cptKqUJ0Phui9Z_kk8zMPrPfV16h0ZfBzKsvIy6_d7cWnn163BMz46kAHtZXqXhNuj19IZRCDfNoqVVxxCIYvbsgInbzZM82CB86iYPAS7piijYn1S6hueVHGAzQorOetZevKIAvbH3kJXZ4KdY6Ffz5SFDJBxC3bycN4q2JM1qnyD53vcc0MitxyIUF7a06iJb5_xXBiA-3xnTI0FU5hw_k6x-sdB5Rglx13_2aNzdWBSBAnxs1XXtZUt9_2RAUxP1XORkrBGlPg9D7cBtQ\",\n" +
                "      \"e\": \"AQAB\",\n" +
                "      \"use\": \"sig\"\n" +
                "    }";
        System.out.println(jwk);

        final PublicKey publicKey = GSAuthRequestUtils.rsaPublicKeyFromJWKString(jwk);
        final String uid = GSAuthRequestUtils.verifyJwt(id_token, API_KEY, publicKey);
    }


}
