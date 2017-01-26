package com.example.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Customized user authentication converter.
 * Converts back and forth from/to JWT token contents and Authentication object.
 */
public class MyUserAuthenticationConverter implements UserAuthenticationConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyUserAuthenticationConverter.class);

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication userAuthentication) {

        LOGGER.info("Contents of authentication {}", userAuthentication);

        return new HashMap<>();
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {

        LOGGER.info("Contents of JWT token {}", map);

        return new UsernamePasswordAuthenticationToken("test", "N/A",
                Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }
}
