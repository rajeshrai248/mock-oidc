package com.realdolmen.oidc.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Oauth2ClientApplication /*extends WebSecurityConfigurerAdapter*/ {
    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers( "/error**")
                .permitAll()
                .anyRequest()
                .authenticated();
    }*/

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ClientApplication.class);
    }
}
