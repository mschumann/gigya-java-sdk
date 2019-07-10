package com.gigya.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class GSAuthRequestUtils {

    /**
     * Generate an RSA private key instance from given Base64 encoded String.
     *
     * @param encodedPrivateKey Base64 encoded private key String resource.
     * @return Generated private key instance.
     */
    public static PrivateKey rsaPrivateKeyFromBase64String(String encodedPrivateKey) {
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
    public static PublicKey rsaPublicKeyFromBase64String(String encodedPublicKey) {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(encodedPublicKey.getBytes()));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (Exception ex) {
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

        // get the private key
        PrivateKey key = rsaPrivateKeyFromBase64String(privateKey);
        if (key == null) {
            // Key generation failed.
            return null;
        }

        // Headers
        final Map<String, Object> header = new HashMap<>();
        header.put("alg", "RS256");
        header.put("typ", "jwt");
        header.put("kid", userKey);

        // Payload.
        final Map<String, Object> claims = new HashMap<>();
        claims.put("iat", Calendar.getInstance(
                TimeZone.getTimeZone("UTC")).getTimeInMillis()
        ); // UTC.
        claims.put("jti", UUID.randomUUID().toString());

        // Compose Jwt.
        String signedJwt = Jwts.builder()
                .setHeader(header)
                .signWith(key, SignatureAlgorithm.RS256)
                .addClaims(claims)
                .compact();

        // TODO: 2019-07-07  Testing only. remove print line.
        System.out.println("jwt:");
        System.out.println(signedJwt);

        return signedJwt;
    }

    /**
     * @param jwt       JWT token to verify
     * @param apiKey    Account ApiKey
     * @return UID from given token if verified. Null otherwise.
     */
    public static String verifyJwt(String jwt, String apiKey, String encodedPublicKey) {
        try {
            // Generate public key instance.
            PublicKey publicKey = rsaPublicKeyFromBase64String(encodedPublicKey);

            // Parse token.
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(jwt);

            // TODO: 2019-07-07  Testing only. remove print line.
            System.out.println(claimsJws);

            // Verification stage.
            // #1 - Verify JWT provided api key with input api key.
            final String jwtApiKey = claimsJws.getBody().get("apiKey", String.class);
            if (jwtApiKey != null && !jwtApiKey.equals(apiKey)) {
                return null;
            }
            // #2 - Verify current time is between iat & exp.
            final long iat = claimsJws.getBody().get("iat", Long.class);
            final long exp = claimsJws.getBody().get("exp", Long.class);
            final long currentTimeInUTC = Calendar.getInstance(
                    TimeZone.getTimeZone("UTC")
            ).getTimeInMillis(); // UTC.
            if (!(currentTimeInUTC >= iat && currentTimeInUTC <= exp)) {
                return null;
            }
            // #3 - Get & return UID field from jwt.
            final String uid = claimsJws.getBody().get("sub", String.class);

            // TODO: 2019-07-07  Testing only. remove print line.
            System.out.println(uid);
            return uid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
