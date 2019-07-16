package com.gigya.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.PrivateKey;
import java.security.PublicKey;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(PowerMockRunner.class)
public class GSAuthRequestUtilsTest {

    private String getPrivateKey() {
        String rawKey = "\\r\\nMIIEoQIBAAKCAQEAy7d/1DUA+2hxI42XxsAsa6zcgOCFJCWfe0wMitRPnBatkx9Z\\r\\nUOGaZQ+UzQxSrMvjiC6C6jLNwYrsyQ6T+KtjlM44IGIdjflh+GM67czaFVWpkihv\\r\\nnWGsC9yV591vpV5qzOTop9QzjRZ52g2xZ4Uws/eNvGMJAx7cvHVQAGBPFXMufmqM\\r\\nNqYvP42hvuik+z+fcRO9l/7u+4YD5D8kROhiLGjdLgoveSojKUfFQDArvKgwlaPm\\r\\nm5uKw6wEzxB/dw1cOJ4mZ50n1USp2zO/OAPoXPZtFyCcHhZnX2aKip5sJ2WiBURS\\r\\nRXV9SeNhXpmbfR28BqB3kaLMG9011hhQJunAdQIDAQABAoH/Wx03jzIlvkx4Te6G\\r\\nc95//6jQ9tN0uTYanorlETLkaGu8NpFO4V4GMC1H4G4ijCalHcOvyg/u9yGrxkE+\\r\\n7Xm6kFOrxtAbZ+SibPX05Wc6I1CtBzIOudV1ndiLM6lWxh/0pK+9X1MJu5DR7zUw\\r\\no5xurA+M1TV2Um77S08s+P4aYPzWdUaVCKVAqd0JTmAVaL9rvpio85ukZRsAqPnq\\r\\nj61mfyW1WWwcuyz4mVZSCk6i7/AcLaXlobzhKzCclmzw8BBPalBkPm8Ftnze5bAF\\r\\ntkC4dTBGJ5vipfBvngGEfcUz1K4xg/VMFnV3+a9dDqRdOpm6ImUGaFKxxlbjk7y1\\r\\npf4hAoGBANP2Rd8/6dKaZgKQdrdvXVd5ms6f1BmDseu4dP8oeZzg3kuvsg3LaTuN\\r\\niSgOogGR7dy64wcQla7b2anfN5SEd+QZdM5C8RXPLy5cUPTXWJ4CEoq/XXhA50+K\\r\\nYEnZ2H7DDRs4sDDj/4Kk2l6n+UdbripNWZpH8jNtQVsX63n+PTBRAoGBAPYKrzSn\\r\\n+TTT2OvwCjWS9+a58v+hfVlfzH+K29xdD9WssTyKWVXgiI6uD0iBwk+hDy60Kufc\\r\\nVBY+3OhKUHgb73cM/0u3ghzgDXhmKWX9ra1grtMK43g/86fqZPkmoJpw55BsGZRs\\r\\nET3dtGkE3f04/aQz+V9oNoRXVi2niPtOOwjlAoGAbTuneovHhl9HXt7wL0xXADON\\r\\nEhrQBW4XDPF8adqEAdLo/HxI+E7xpl0kZ95Soxh/SpeNVCC54ukt3RtJ22IBHy33\\r\\nQizBbYneOLgeiG9KHfPXdmV0V/qquhUH1kdMCNegM30dX7TAAqXOW6WZE6stsGM4\\r\\nYAffy5zsZ3OGSNI62mECgYEAhR04t3Ndh7BJ/zRKQbv29VSLXLSBPdZvrF1zICEV\\r\\nTRR2e3uaY7TsDM2tJRxXBX/s1+bQA8uXjsWJ/P8f1CvA/hcBFHT/JyItB4O2SCDc\\r\\nUx4o17NfaBKpf6J36Lh2UbheACwMFtn05hTJ47unYrXvvGQGOhEG1cjurhqjKNul\\r\\nYuUCgYAhkvsyR0PaXNtIAlhdFX2V54wvFOuWCvez7Mk5unsXCYFq5Uoar7CMBBl9\\r\\n2H0z6+6HLhPn8k4H3SqNecAQlzzc3gr05F/AFkEivgLY7DsaYmj8aw/V1qi/Pbh4\\r\\npXBvExL/B937okJfR8aOzNWwUEA/tLvUejrdbUSHu0bNsB4/cQ==";
        return stripUnwantedDelimiters(rawKey);
    }

    private String getPublicKey() {
        String rawKey = "\\r\\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy7d/1DUA+2hxI42XxsAs\\r\\na6zcgOCFJCWfe0wMitRPnBatkx9ZUOGaZQ+UzQxSrMvjiC6C6jLNwYrsyQ6T+Ktj\\r\\nlM44IGIdjflh+GM67czaFVWpkihvnWGsC9yV591vpV5qzOTop9QzjRZ52g2xZ4Uw\\r\\ns/eNvGMJAx7cvHVQAGBPFXMufmqMNqYvP42hvuik+z+fcRO9l/7u+4YD5D8kROhi\\r\\nLGjdLgoveSojKUfFQDArvKgwlaPmm5uKw6wEzxB/dw1cOJ4mZ50n1USp2zO/OAPo\\r\\nXPZtFyCcHhZnX2aKip5sJ2WiBURSRXV9SeNhXpmbfR28BqB3kaLMG9011hhQJunA\\r\\ndQIDAQAB\\r\\n";
        return stripUnwantedDelimiters(rawKey);
    }

    private String stripUnwantedDelimiters(String raw) {
        return raw.replace("\\r", "").replace("\\n", "");
    }

    @Test
    public void test_rsaPrivateKeyFromBase64String() {
        // Act
        final PrivateKey key = GSAuthRequestUtils.rsaPrivateKeyFromBase64String(getPrivateKey());
        // Assert
        assertNotNull(key);
        assertEquals("RSA", key.getAlgorithm());
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
    public void test_validateGigyaSignature() {

    }

}
