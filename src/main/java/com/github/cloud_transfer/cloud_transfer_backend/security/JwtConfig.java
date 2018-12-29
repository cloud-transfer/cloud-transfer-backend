package com.github.cloud_transfer.cloud_transfer_backend.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Getter
@Component
public class JwtConfig {
    @Value("${security.jwt.uri:/login/**}")
    private String Uri;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration:#{24*60*60}}")
    private int expiration;

    private byte[] secret;

    @Value("${security.jwt.secret:3725606056862686781680664477818556036911465311694522372184618344144923851834233021850798498213876926474487018178194626594751682831307142785586986395345840}")
    private void setSecret(String secret) {
        BigInteger bigInteger = new BigInteger(secret);
        this.secret = bigInteger.toByteArray();
    }
}