package com.juju.tistar.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequest {
    private final String  name;
    private final String pwd;
}
