package com.example.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Just a test controller.
 */
@RestController
public class HelloWorldRestController {

    @RequestMapping(path = "/restricted")
    public String sayHelloProtected() {
        return "Access to protected resource granted";
    }

    @RequestMapping(path = {"/", "/unrestricted"})
    public String sayHelloUnProtected() {
        return "Unprotected resource";
    }

}
