package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtCreatorApplicationTests {

    @Value("${jwt.keystore}")
    private String keystorePath;

    @Autowired
	private OpenIdConnectTokenCreator openIdConnectTokenCreator;

	@Test
	public void verifyCreateAndVerifyToken() {

        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(new ClassPathResource(keystorePath), "secret".toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("jwtdemo");
        PrivateKey privateKey = keyPair.getPrivate();
        Assert.state(privateKey instanceof RSAPrivateKey, "KeyPair must be an RSA ");
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        SignatureVerifier verifier = new RsaVerifier(publicKey);

        Jwt generatedToken = openIdConnectTokenCreator.generateToken(
                "mysubject", "Donald", "Duck", Arrays.asList("USER", "ADMIN"));
        assertThat(generatedToken).isNotNull();

        // Should be verified successfully
        generatedToken.verifySignature(verifier);

        assertThat(generatedToken.getClaims()).isNotNull();
        JsonParser jsonParser = JsonParserFactory.create();
        Map<String, Object> claimsMap = jsonParser.parseMap(generatedToken.getClaims());
        assertThat(claimsMap).isNotNull().hasSize(10);
        assertThat(claimsMap).containsKeys("aud", "sub", "roles", "family_name", "given_name");
    }

}
