package com.realdolmen.oidc.client.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallbackController {

    @GetMapping(path = "/callback")
    public void verifyCodeResponse(@RequestParam(name = "code") final String code, @RequestParam(name = "state") final String state) {
        System.out.println(String.format("received code: %s , state: %s", code, state));
    }
}
