package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Token creator to create compliant JWT tokens according to
 * OpenId Connect 1.0 core standard.
 *
 * <ul>
 * <li>iss (REQUIRED). Issuer Identifier for the Issuer of the response.
 * The iss value is a case sensitive URL using the https scheme that contains scheme, host, and optionally,
 * port number and path components and no query or fragment components.</li
 * <li>sub (REQUIRED): Subject Identifier.
 * A locally unique and never reassigned identifier within the Issuer for the End-User, which is intended to
 * be consumed by the Client, e.g., 24400320 or AItOawmwtWwcT0k51BayewNvutrJUqsvl6qs7A4.
 * It MUST NOT exceed 255 ASCII characters in length. The sub value is a case sensitive string.</li>
 * <li>aud (REQUIRED): Audience(s) that this ID Token is intended for.
 * It MUST contain the OAuth 2.0 client_id of the Relying Party as an audience value.
 * It MAY also contain identifiers for other audiences. In the general case, the aud value is an array of
 * case sensitive strings. In the common special case when there is one audience,
 * the aud value MAY be a single case sensitive string.</li>
 * <li>exp (REQUIRED): Expiration time on or after which the ID Token MUST NOT be accepted for processing.
 * The processing of this parameter requires that the current date/time MUST be before the expiration date/time
 * listed in the value. Implementers MAY provide for some small leeway, usually no more than a few minutes,
 * to account for clock skew. Its value is a JSON number representing the number of seconds from 1970-01-01T0:0:0Z
 * as measured in UTC until the date/time. See RFC 3339 [RFC3339] for details regarding date/times in general
 * and UTC in particular.</li>
 * <li>iat (REQUIRED): Time at which the JWT was issued. Its value is a JSON number representing the number of
 * seconds from 1970-01-01T0:0:0Z as measured in UTC until the date/time.</li>
 * <li>auth_time Time when the End-User authentication occurred. Its value is a JSON number representing the number
 * of seconds from 1970-01-01T0:0:0Z as measured in UTC until the date/time. When a max_age request is made or
 * when auth_time is requested as an Essential Claim, then this Claim is REQUIRED; otherwise, its inclusion is
 * OPTIONAL. (The auth_time Claim semantically corresponds to the OpenID 2.0 PAPE [OpenID.PAPE] auth_time
 * response parameter.)</li>
 * <li>nonce: String value used to associate a Client session with an ID Token, and to mitigate replay attacks.
 * The value is passed through unmodified from the Authentication Request to the ID Token. If present in the
 * ID Token, Clients MUST verify that the nonce Claim Value is equal to the value of the nonce parameter sent in the
 * Authentication Request. If present in the Authentication Request, Authorization Servers MUST include a nonce Claim
 * in the ID Token with the Claim Value being the nonce value sent in the Authentication Request. Authorization
 * Servers SHOULD perform no other processing on nonce values used. The nonce value is a case sensitive string.</li>
 * </ul>
 */
@Component
public class OpenIdConnectTokenCreator {

    // Mandatory standard claims
    private static final String ISSUER_KEY = "iss";
    private static final String SUBJECT_KEY = "sub";
    private static final String AUDIANCE_KEY = "aud";
    private static final String EXPIRATION_TIME_KEY = "exp";
    private static final String ISSUE_TIME_KEY = "iat";
    private static final String AUTH_TIME_KEY = "auth_time";
    private static final String NONCE_KEY = "nonce";

    // Optional standard claims
    private static final String GIVEN_NAME_KEY = "given_name";
    private static final String FAMILY_NAME_KEY = "family_name";
    private static final String EMAIL_KEY = "email";

    // Custom claim
    private static final String ROLES_KEY = "roles";

    // Constant values for issuer, audiance and nonce
    private static final String ISSUER = "https://jwtcreator.example.com";
    private static final String AUDIANCE = "oauth2-resource";
    private static final String NONCE = "12345";

    private final JwtCreator jwtCreator;

    @Autowired
    public OpenIdConnectTokenCreator(JwtCreator jwtCreator) {
        this.jwtCreator = jwtCreator;
    }

    public Jwt generateToken(String subject, String firstName, String lastName, List<String> roles) {

        Map<String, Object> tokenEntries = new HashMap<>();
        tokenEntries.put(ISSUER_KEY, ISSUER);
        tokenEntries.put(AUDIANCE_KEY, AUDIANCE);
        tokenEntries.put(NONCE_KEY, NONCE);
        tokenEntries.put(ISSUE_TIME_KEY, Math.abs(System.currentTimeMillis() / 1000));
        tokenEntries.put(AUTH_TIME_KEY, Math.abs(System.currentTimeMillis() / 1000));
        tokenEntries.put(EXPIRATION_TIME_KEY, Math.abs(System.currentTimeMillis() + 10*60*1000) / 1000);
        tokenEntries.put(SUBJECT_KEY, subject);
        tokenEntries.put(GIVEN_NAME_KEY, firstName);
        tokenEntries.put(FAMILY_NAME_KEY, lastName);
        tokenEntries.put(ROLES_KEY, roles);

        return jwtCreator.generateToken(tokenEntries);
    }
}
