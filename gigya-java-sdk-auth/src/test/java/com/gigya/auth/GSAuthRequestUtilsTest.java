package com.gigya.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import static junit.framework.TestCase.*;

@RunWith(PowerMockRunner.class)
public class GSAuthRequestUtilsTest {

    private String getPrivateKey() {
        String rawKey = "-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCW+no9nd6J++uc\\nhX/ak2x1uFg7QXk4FWF676rNoxJlyMSRSj25JIhJe2BcFaS78NRxnUywS51AU/3f\\nxNVEwphT1MEE2CDs0pclSmhPUxnN+ep17AYseIyg0FHZC5N2WJMBcnuYm/NjKlYr\\nlUtndH7uGOxuAM0X2Jy4GpHGQZwrw6SFzMYHYnQhmIZ311iBB00nquPHRXDhQpBb\\n3O9qvOWSFbOg1nY+Xgp9J0OyEN64j9tt+7i+sMSuOfFHrlxigawA9bXo+p7HZVON\\nf4Ne48pOd5bIqmaRwIFw5L3HEfE4JXnmslEGEZKNPZ5U2hKCEU/8fvipv8518TaS\\ntTCmmPbFAgMBAAECggEACLIDYVIa04qhTsH3k6CjAgKsfjkuoJbGpvxvs2k9cYRH\\nIfELLgMXIFhNO/B5LOPZcHO1S4AHXgGYYQ9mkw+8EzPxK/TArLMSRnELIepw1Yab\\nxM/jqSMGQmrNE/mRNCM00EQGL0toEKGLFfaCwcIZX2ArGcjNBx8QI6BQHgGAW5rR\\n0KhlKGG2LMVYNXLFWS9D7cqSD5tpUvbPNZbVj6LR7gtrxVcQO6f4ikM6mB3u3tjB\\npDlmm0XzkuTcJ+zgd+a8aSqgJFQfIp7vpMY6Ntc52MUDnVz+pX25pYXP/0JmdFJJ\\nvXy87t8ua1yKLtYnZWzZNXeoKhvYXKRPsz4L+FBnMQKBgQC9CpJtC3ZYzW3Jx56b\\nXvQGx++zFItJAkZbifxnkpuu4Cf5BSMijBQRTo/IX5fpkxZySjoxiBV0XQ81f1Kp\\nx0hn3f7MJOIgt2mCwN+/IKfMMz35y5QchAQtriAcLv73bk/NVdQstxR8QSK1nzyu\\njwemv1MJImMNaXfT9H09WpXwHQKBgQDMdIh1fC98UVEpFZ6FlrCvh9Ch6uMO2kMs\\nAng94nsb+1LmeOsSrDR1wg5Izdd4d7xRxpjyf5S2GMawvfHvHrg+nZZHD3azqRcq\\nG9Eb5oI2D6L8J0QRy4PAxTZnZYTE56kcHfE0pYYSKDqubGkMfEvHq5jz/LJIFhfm\\nSzg9viIwyQKBgD2gla2w3+sBRXpTdlRmdx1CztTNrL6nXDJ5YGyPcetnrgBTeWnI\\nyr39o4gKCeceiWHG6wO8vmnJ8KxbDqLEkckyqN76YzGROXdj001mou1CA0FM6cMG\\nEqqlqlglxf752lAxW9Mb+DSts1gMSmcJv8/PbY17xVjY+jSB7tYyktDNAoGAM2v/\\nwiS4wLinBAFG/GxZBdzP0VmFQAAPMutwGIh01CXSxNqWrPyYuSFUfGUhE1ByEdM0\\nNpF18pKqrlsnlS+RwVXbLQYroaYeiF20qyK/jx9Bq8+oZB1ehsZTF5BF40wskUDK\\noKYc4UYy9BmaFiTQ3mg/MOZWZKEB4875VzYR3VkCgYEAgUPhkUCfGc1OWru6WcPq\\n0mq3F8rCvQndyz9Jn1orXLojP1u8nJF+jCiMuVnVsc1ChwiyK652MkXjKBL6kqG+\\nHSLv/UZhvG1XJKaUB7q48yryBqpDHcNLOu11YI+Oyd+Dict6xVFrt36n3uWOLATB\\nSyZDOsqX1TEitG5BdJQHIJc=\\n-----END PRIVATE KEY-----\\n";
        return stripUnwantedDelimiters(rawKey);
    }

    private String getPublicKey() {
        String rawKey = "-----BEGIN PUBLIC KEY-----\\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlvp6PZ3eifvrnIV/2pNs\\ndbhYO0F5OBVheu+qzaMSZcjEkUo9uSSISXtgXBWku/DUcZ1MsEudQFP938TVRMKY\\nU9TBBNgg7NKXJUpoT1MZzfnqdewGLHiMoNBR2QuTdliTAXJ7mJvzYypWK5VLZ3R+\\n7hjsbgDNF9icuBqRxkGcK8OkhczGB2J0IZiGd9dYgQdNJ6rjx0Vw4UKQW9zvarzl\\nkhWzoNZ2Pl4KfSdDshDeuI/bbfu4vrDErjnxR65cYoGsAPW16Pqex2VTjX+DXuPK\\nTneWyKpmkcCBcOS9xxHxOCV55rJRBhGSjT2eVNoSghFP/H74qb/OdfE2krUwppj2\\nxQIDAQAB\\n-----END PUBLIC KEY-----\\n";
        return stripUnwantedDelimiters(rawKey);
    }

    private String stripUnwantedDelimiters(String raw) {
        return raw.replace("\\r", "").replace("\\n", "");
    }

    @Test
    public void test_rsaPrivateKeyFromBase64StringPKCS8() {
        final String key = getPrivateKey();
        final PrivateKey privateKey = GSAuthRequestUtils.rsaPrivateKeyFromBase64String(key);
        assertNotNull(key);
        assertEquals("RSA", privateKey.getAlgorithm());
    }

    @Test
    public void test_rsaPublicKeyFromBase64String() {
        // Act
        final PublicKey key = GSAuthRequestUtils.rsaPublicKeyFromBase64String(getPublicKey());
        // Assert
        assertNotNull(key);
        assertEquals("RSA", key.getAlgorithm());
    }

    @Test
    public void test_rsaPublicKeyFromJWKString() {
        // Arrange
        final String jwk = "{\n" +
                "      \"alg\": \"RS256\",\n" +
                "      \"kid\": \"REQ0MUQ5N0NCRTJEMzk3M0U1RkNDQ0U0Q0M1REFBRjhDMjdENUFBQg\",\n" +
                "      \"kty\": \"RSA\",\n" +
                "      \"n\": \"qoQah4MFGYedrbWwFc3UkC1hpZlnB2_E922yRJfHqpq2tTHL_NvjYmssVdJBgSKi36cptKqUJ0Phui9Z_kk8zMPrPfV16h0ZfBzKsvIy6_d7cWnn163BMz46kAHtZXqXhNuj19IZRCDfNoqVVxxCIYvbsgInbzZM82CB86iYPAS7piijYn1S6hueVHGAzQorOetZevKIAvbH3kJXZ4KdY6Ffz5SFDJBxC3bycN4q2JM1qnyD53vcc0MitxyIUF7a06iJb5_xXBiA-3xnTI0FU5hw_k6x-sdB5Rglx13_2aNzdWBSBAnxs1XXtZUt9_2RAUxP1XORkrBGlPg9D7cBtQ\",\n" +
                "      \"e\": \"AQAB\",\n" +
                "      \"use\": \"sig\"\n" +
                "    }";
        // Act
        final PublicKey key = GSAuthRequestUtils.rsaPublicKeyFromJWKString(jwk);
        // Assert
        assertNotNull(key);
        assertEquals("RSA", key.getAlgorithm());
    }

    @Test
    public void test_compose() {
        // Act
        final String jws = GSAuthRequestUtils.composeJwt("ANiHp6OEaqFZ", getPrivateKey());
        System.out.println(jws);
        // Assert
        assertNotNull(jws);
    }

    @Test
    public void test_generateTokenAndVerify() {
        // Arrange
        final PublicKey publicKey = GSAuthRequestUtils.rsaPublicKeyFromBase64String(getPublicKey());
        // Act
        final String jws = GSAuthRequestUtils.composeJwt("ANiHp6OEaqFZ", getPrivateKey());
        final Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(jws);
        // Assert
        assertNotNull(claimsJws);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_generateTokenAndParseWithoutPublicKey() {
        // Act
        final String jws = GSAuthRequestUtils.composeJwt("ANiHp6OEaqFZ", getPrivateKey());
        final Jws<Claims> claimsJws = Jwts.parser().parseClaimsJws(jws);
    }

    @Test
    public void test_generateTokenAndParseHeadersWithoutKey() throws JSONException {
        final String jws = GSAuthRequestUtils.composeJwt("ANiHp6OEaqFZ", getPrivateKey());

        assertNotNull(jws);

        String[] split = jws.split("\\.");
        assertTrue(split.length > 0);

        final String encodedHeaders = split[0];
        final String decodedHeaders = new String(Base64.getDecoder().decode(encodedHeaders.getBytes()));
        assertNotNull(decodedHeaders);
        System.out.println(decodedHeaders);

        final JSONObject jsonObject = new JSONObject(decodedHeaders);
        final String kid = jsonObject.getString("kid");

        assertNotNull(kid);
        System.out.println(kid);
    }

    @Test
    public void test_fetchPublicJWK() {

        final String jwk = GSAuthRequestUtils.fetchPublicJWK(
                "REQ0MUQ5N0NCRTJEMzk3M0U1RkNDQ0U0Q0M1REFBRjhDMjdENUFBQg",
                "3_okzFVIQTsXw5vS6s0y9BEm6T4fbNTPVox6DZAwn-rCC7ca1dv6LhrPCdksCiSfOc",
                "us1-st2.gigya.com");

        assertNotNull(jwk);
    }

    @Test
    public void test_fetchPublicJWK_withWrongKid() {

        final String jwk = GSAuthRequestUtils.fetchPublicJWK(
                "REQ0MUQ5N0NCRTJEMzk3M0U1RkNDQ0U0Q0M1REFBRjhDMjdENUFBQgss",
                "3_okzFVIQTsXw5vS6s0y9BEm6T4fbNTPVox6DZAwn-rCC7ca1dv6LhrPCdksCiSfOc",
                "us1-st2.gigya.com");

        assertNull(jwk);
    }

}
