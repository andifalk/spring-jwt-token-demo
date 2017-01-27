# JSON web token (JWT) creator

## Intro
This project provides two creator implementations:

* JwtCreator: This creates RSA signed JWT tokens with custom content
* OpenIdConnectTokenCreator: This is based on the JwtCreator and creates RSA signed tokens with all required 
claims according to OpenId Connect 1.0 standard.
 
All tokens are signed with RSA asymmetric private/public key via provided java keystore.
  
## System requirements
This project requires a java 8 vm with Java Cryptography Extension (JCE).

## Usage

To generate an OpenId Connect JWT token just use the main starter class _JwtCreatorApplication_.

There you find the call to actually generate a JWT token. You can fill in your required values like subject,
first name, last name and specific roles. If you need different contents you may change the implementation. 

```java
openIdConnectTokenCreator.generateToken(String subject,
                         String firstName,
                         String lastName,
                         List<String> roles)
```  
  
After running the class _JwtCreatorApplication_ you find console output of the following artifacts:
  
1. The public to verify the signature of the generated JWT token
2. The created JWT token in clear text (to see the values)
3. The base64 encoded JWT token (to be used as bearer token in _Authorization_ header)  

