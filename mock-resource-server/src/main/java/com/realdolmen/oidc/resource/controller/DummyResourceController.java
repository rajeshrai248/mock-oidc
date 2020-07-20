package com.realdolmen.oidc.resource.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyResourceController {
    @GetMapping(path = "/checkmycredentials")
    public ResponseEntity<String> giveOkResponseToRightGuy() {
        return ResponseEntity.ok("**************Welkom**************");
    }
}
