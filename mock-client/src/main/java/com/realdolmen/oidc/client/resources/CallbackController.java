package com.realdolmen.oidc.client.resources;

import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CallbackController {

    private final RestTemplate oauth2RestTemplate;

    @Autowired
    public CallbackController(@Qualifier("oauth2RestTemplate") RestTemplate oauth2RestTemplate) {
        this.oauth2RestTemplate = oauth2RestTemplate;
    }

    @GetMapping(path = "/api/login")
    public ResponseEntity<OAuth2AccessTokenResponse> verifyCodeResponse(@RequestParam(
            name = "code") final String code, @RequestParam(
            name = "state") final String state) {
        log.info(String.format("received code: %s , state: %s", code, state));
        return oauth2RestTemplate.getForEntity("", OAuth2AccessTokenResponse.class, ImmutableMap.of("code", code, "state", state));

    }

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

}
