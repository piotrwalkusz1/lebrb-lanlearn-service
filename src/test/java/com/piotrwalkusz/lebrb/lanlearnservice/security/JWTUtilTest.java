package com.piotrwalkusz.lebrb.lanlearnservice.security;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;

public class JWTUtilTest {

    private String secret = "PasW0!d";

     /*
     *    {
     *      "alg": "HS256",
     *      "typ": "JWT"
     *    }
     *    {
     *      "name": "John Brown",
     *      "role": "user"
     *    {
     */
    private String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiSm9obiBCcm93biIsInJvbGUiOiJ1c2VyIn0.UVcayXp8GU0ulfkO9kmwjWkhhbzMa13j0NXnuTCxzh4";

    private String invalidToken = validToken.substring(0, validToken.length() - 5);

    /*
     *    {
     *      "alg": "HS256",
     *      "typ": "JWT"
     *    }
     *    {
     *      "name": "John Brown",
     *      "role": "user"
     *      "exp": 1517424245
     *    {
     */
    private String unexpiredToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiSm9obiBCcm93biIsInJvbGUiOiJ1c2VyIiwiZXhwIjoxNTE3NDI0MjQ1fQ.S_wNFvWhNN_TFq3mdTCqiAA91DFuh3-x4oaxmARmUGo";

    /*
    *    {
    *      "alg": "HS256",
    *      "typ": "JWT"
    *    }
    *    {
    *      "name": "John Brown",
    *      "role": "user"
    *      "exp": 1517424230
    *    {
    */
    private String expiredToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiSm9obiBCcm93biIsInJvbGUiOiJ1c2VyIiwiZXhwIjoxNTE3NDI0MjMwfQ.vYX71flFhpf6bPuoGdHCVzofillAm5uLokaILmtCoIc";

    private Clock clock = Clock.fixed(Instant.ofEpochSecond(1517424241), ZoneId.systemDefault());

    private JWTUtil jwtUtil = new JWTUtil(secret, clock);

    @Test
    public void successfulValidationAndDecoding() {
        DecodedJWT decodedToken = jwtUtil.verifyAndDecode(validToken);

        assertEquals("HS256", decodedToken.getHeaderClaim("alg").asString());
        assertEquals("JWT", decodedToken.getHeaderClaim("typ").asString());
        assertEquals("John Brown", decodedToken.getClaim("name").asString());
        assertEquals("user", decodedToken.getClaim("role").asString());
    }

    @Test(expected = SignatureVerificationException.class)
    public void invalidSignature() {
        jwtUtil.verifyAndDecode(invalidToken);
    }

    @Test
    public void unexpiredToken() {
        jwtUtil.verifyAndDecode(unexpiredToken);
    }

    @Test(expected = TokenExpiredException.class)
    public void expiredToken() {
        DecodedJWT jwt = jwtUtil.verifyAndDecode(expiredToken);
    }
}