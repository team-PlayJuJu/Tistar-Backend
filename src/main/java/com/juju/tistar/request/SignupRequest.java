package com.juju.tistar.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.juju.tistar.entity.Authority;
import com.juju.tistar.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SignupRequest {

    @NotNull
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String pwd;

    private Set<AuthorityRequest> authority;

    public static SignupRequest from(User user) {
        if(user == null) return null;

        return SignupRequest.builder()
                .name(user.getName())
                .authority(user.getAuthorities().stream()
                        .map(authority -> AuthorityRequest.builder().name(authority.getName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}