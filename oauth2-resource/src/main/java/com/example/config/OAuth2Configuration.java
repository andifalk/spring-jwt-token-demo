package com.example.config;

import com.example.oauth.MyUserAuthenticationConverter;
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * OAuth configuration.
 */
@EnableResourceServer
@Configuration
public class OAuth2Configuration {

    @Bean
    public JwtAccessTokenConverterConfigurer jwtAccessTokenConverterConfigurer() {
        return new MyJwtConfigurer();
    }

    public static class MyJwtConfigurer implements JwtAccessTokenConverterConfigurer {

        /**
         * Constructor.
         */
        MyJwtConfigurer() {
        }

        @Override
        public void configure(JwtAccessTokenConverter converter) {
            MyUserAuthenticationConverter userAuthenticationConverter = new
                    MyUserAuthenticationConverter();
            DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
            defaultAccessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
            converter.setAccessTokenConverter(defaultAccessTokenConverter);
        }
    }
}
