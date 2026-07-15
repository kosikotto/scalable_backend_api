
package com.example.demo.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.*;
import java.util.stream.Collectors;

public class KeycloakJwtAuthConverter extends JwtAuthenticationConverter {

    public KeycloakJwtAuthConverter() {
        setJwtGrantedAuthoritiesConverter(new KeycloakRoleExtractor());
    }

    public static class KeycloakRoleExtractor implements Converter<Jwt, Collection<GrantedAuthority>> {

        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            System.out.println("JWT claims: " + jwt.getClaims());
            List<String> realmRoles = Optional
                    .ofNullable(jwt.getClaimAsMap("realm_access"))
                    .map(r -> (List<String>) r.get("roles"))
                    .orElse(Collections.emptyList());

            return realmRoles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        }
    }
}
