package com.piotrwalkusz.lebrb.lanlearnservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.Clock;
import java.util.Date;

@Service
public class JWTUtil {

    private JWTVerifier verifier;

    @Autowired
    public JWTUtil(@Value("${jwt.secret}") String secret) {
        this(secret, null);
    }

    public JWTUtil(String secret, Clock clock) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Verification builder = JWT.require(algorithm);
            if (clock != null) {
                com.auth0.jwt.interfaces.Clock customClock = () -> Date.from(clock.instant());
                verifier = ((JWTVerifier.BaseVerification) builder).build(customClock);
            } else {
                verifier = builder.build();
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public DecodedJWT verifyAndDecode(String token) {
        return verifier.verify(token);
    }
}
