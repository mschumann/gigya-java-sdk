package com.gigya.auth;

import com.gigya.socialize.GSLogger;
import com.gigya.socialize.GSRequest;
import com.gigya.socialize.GSResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONObject;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class GSAuthRequestUtils {

    public static GSLogger logger = new GSLogger();

    /**
     * Generate an RSA private key instance from given Base64 encoded String.
     *
     * @param encodedPrivateKey Base64 encoded private key String resource (RSA - PKCS#1).
     * @return Generated private key instance.
     */
    static PrivateKey rsaPrivateKeyFromBase64String(String encodedPrivateKey) {
        try {

            DerInputStream derReader = new DerInputStream(Base64.getDecoder().decode(encodedPrivateKey.getBytes()));
            DerValue[] seq = derReader.getSequence(0);
            // skip version seq[0];
            BigInteger modulus = seq[1].getBigInteger();
            BigInteger publicExp = seq[2].getBigInteger();
            BigInteger privateExp = seq[3].getBigInteger();
            BigInteger prime1 = seq[4].getBigInteger();
            BigInteger prime2 = seq[5].getBigInteger();
            BigInteger exp1 = seq[6].getBigInteger();
            BigInteger exp2 = seq[7].getBigInteger();
            BigInteger crtCoef = seq[8].getBigInteger();

            RSAPrivateCrtKeySpec keySpec =
                    new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2, exp1, exp2, crtCoef);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception ex) {
            logger.write(ex);
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Generate an RSA public key instance from given Base64 encoded String.
     *
     * @param encodedPublicKey ase64 encoded public key String resource.
     * @return Generated public key instance.
     */
    static PublicKey rsaPublicKeyFromBase64String(String encodedPublicKey) {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(encodedPublicKey.getBytes()));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (Exception ex) {
            logger.write(ex);
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Generate an RSA public key instance from JWK String.
     *
     * @param jwk JWK String.
     * @return Generated public key instance.
     */
    static PublicKey rsaPublicKeyFromJWKString(String jwk) {
        try {
            // JWK to json.
            JSONObject jsonObject = new JSONObject(jwk);
            final String n = jsonObject.getString("n");
            final String e = jsonObject.getString("e");

            KeyFactory kf = KeyFactory.getInstance("RSA");
            BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n)); //n
            BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e)); //e
            return kf.generatePublic(new RSAPublicKeySpec(modulus, exponent));
        } catch (Exception ex) {
            logger.write(ex);
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Compost a JWT given account userKey & privateKey.
     *
     * @param userKey    Account user key.
     * @param privateKey Account Base64 encoded private key.
     * @return Generated JWT String.
     */
    public static String composeJwt(String userKey, String privateKey) {

        // #1 - Decode RSA private key (PKCS#1).
        final PrivateKey key = rsaPrivateKeyFromBase64String(privateKey);
        if (key == null) {
            logger.write("Failed to instantiate private key from Base64");
            // Key generation failed.
            return null;
        }

        // #2 - Add JWT headers.
        final Map<String, Object> header = new HashMap<>();
        header.put("alg", "RS256");
        header.put("typ", "jwt");
        header.put("kid", userKey);

        // #3 - Add JWT payload.
        final Map<String, Object> claims = new HashMap<>();
        claims.put("iat", Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()); // UTC.
        claims.put("jti", UUID.randomUUID().toString());

        // #4 - Compose & sign Jwt.
        return Jwts.builder()
                .setHeader(header)
                .signWith(key, SignatureAlgorithm.RS256)
                .addClaims(claims)
                .compact();
    }

    /**
     * Verify Gigya Id Token.
     *
     * @param jwt       Id token
     * @param apiKey    Client ApiKey
     * @param apiSecret Client ApiSecret
     */
    public static boolean validateGigyaSignature(String jwt, String apiKey, String apiSecret) {
        // Fetch the public key.
        final GSRequest request = new GSRequest(apiKey, apiSecret, "accounts.getJWTPublicKey");
        final GSResponse response = request.send();
        if (response.getErrorCode() == 0) {
            final String jwk = response.getData().toJsonString();
            if (jwk == null) {
                logger.write("Failed to obtain JWK from response data");
                return false;
            }
            final PublicKey publicKey = GSAuthRequestUtils.rsaPublicKeyFromJWKString(jwk);
            if (publicKey == null) {
                logger.write("Failed to instantiate private key from JWK");
                return false;
            }
            return verifyJwt(jwt, apiKey, publicKey);
        }
        return false;
    }

    /**
     * @param jwt    JWT token to verify.
     * @param apiKey Account ApiKey.
     * @return UID from given token if verified. Null otherwise.
     */
    public static boolean verifyJwt(String jwt, String apiKey, PublicKey key) {
        try {

            // #1 - Parse token.
            final Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwt);

            // Verification stage.
            // #2 - Verify JWT provided api key with input api key.
            final String jwtApiKey = claimsJws.getBody().get("apiKey", String.class);
            if (jwtApiKey != null && !jwtApiKey.equals(apiKey)) {
                logger.write("JWT verification failed - apiKey does not match");
                return false;
            }

            // #3 - Verify current time is between iat & exp.
            final long iat = claimsJws.getBody().get("iat", Long.class);
            final long exp = claimsJws.getBody().get("exp", Long.class);
            final long currentTimeInUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis(); // UTC.
            if (!(currentTimeInUTC >= iat && currentTimeInUTC <= exp)) {
                logger.write("JWT verification failed - expired");
                return false;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
