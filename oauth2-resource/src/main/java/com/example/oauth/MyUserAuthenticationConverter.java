package com.example.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Customized user authentication converter.
 * Converts back and forth from/to JWT token contents and Authentication object.
 */
public class MyUserAuthenticationConverter implements UserAuthenticationConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyUserAuthenticationConverter.class);

    private static final String ROLES_KEY = "roles";
    private static final String ISSUER_KEY = "iss";
    private static final String SUBJECT_KEY = "sub";
    private static final String AUDIANCE_KEY = "aud";
    private static final String ISSUER = "https://jwtcreator.example.com";
    private static final String AUDIANCE = "oauth2-resource";

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication userAuthentication) {

        LOGGER.info("Contents of authentication {}", userAuthentication);

        return new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {

        LOGGER.info("Contents of JWT token {}", map);

        if (!map.containsKey(ISSUER_KEY) || !map.get(ISSUER_KEY).equals(ISSUER)) {
            LOGGER.warn("Wrong issuer {} detected", map.get(ISSUER_KEY));
            throw new UsernameNotFoundException("Issuer is invalid");
        }

        if (!map.containsKey(AUDIANCE_KEY) || !map.get(AUDIANCE_KEY).equals(AUDIANCE)) {
            LOGGER.warn("Wrong audiance {} detected", map.get(AUDIANCE_KEY));
            throw new UsernameNotFoundException("Audiance is invalid");
        }

        if (!map.containsKey(SUBJECT_KEY)) {
            throw new UsernameNotFoundException("Subject is missing");
        }

        List<GrantedAuthority> authorities =new ArrayList<>();

        if (map.containsKey(ROLES_KEY)) {
            Object obj = map.get(ROLES_KEY);
            if (obj instanceof List) {
                ((List<String>) obj).forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
            }
        }

        return new UsernamePasswordAuthenticationToken(map.get(SUBJECT_KEY), "N/A", authorities);
    }
}
