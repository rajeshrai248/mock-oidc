package com.realdolmen.oidc.client.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyResourceController {
    @GetMapping(path = "/demo/hi")
    public ResponseEntity<String> verifyCodeResponse() {
        return ResponseEntity.ok("Hi There");

    }
}
