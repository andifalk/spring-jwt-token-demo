package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.*;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.security.crypto.codec.Base64;

import javax.annotation.PostConstruct;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * Creates JWT tokens.
 */
@Component
public class JwtCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtCreator.class);

    @Value("${jwt.keystore}")
    private String keystorePath;

    private String verifierKey;
    private Signer signer;
    private SignatureVerifier verifier;

    private JsonParser objectMapper = JsonParserFactory.create();

    @PostConstruct
    public void initKeyPair() {
        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(new ClassPathResource(keystorePath), "secret".toCharArray());
        setKeyPair(keyStoreKeyFactory.getKeyPair("jwtdemo"));
    }

    private void setKeyPair(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        Assert.state(privateKey instanceof RSAPrivateKey, "KeyPair must be an RSA ");
        signer = new RsaSigner((RSAPrivateKey) privateKey);
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        verifier = new RsaVerifier(publicKey);
        verifierKey = "-----BEGIN PUBLIC KEY-----\n" + new String(Base64.encode(publicKey.getEncoded()))
                + "\n-----END PUBLIC KEY-----";

        LOGGER.info("Public key is: ");
        LOGGER.info(verifierKey);
    }

    public Jwt generateToken(Map<String,?> valueMap) {

        String content;
        try {
            content = objectMapper.formatMap(valueMap);
        }
        catch (Exception e) {
            throw new IllegalStateException("Cannot convert access token to JSON", e);
        }

        return JwtHelper.encode(content, signer);
    }
}
