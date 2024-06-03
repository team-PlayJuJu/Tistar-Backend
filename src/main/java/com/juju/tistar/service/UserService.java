package com.juju.tistar.service;

import com.juju.tistar.config.TokenProvider;
import com.juju.tistar.entity.Authority;
import com.juju.tistar.entity.User;
import com.juju.tistar.repository.AuthorityRepository;
import com.juju.tistar.repository.UserRepository;
import com.juju.tistar.request.LoginRequest;
import com.juju.tistar.response.LoginResponse;
import com.mysql.cj.exceptions.PasswordExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;


    @Transactional
    public LoginRequest signupUser(LoginRequest request) {
        if(!userRepository.existsUserByName(request.getName())) {
            Authority authority = Authority.builder()
                    .role("ROLE_USER")
                    .build();
            authorityRepository.save(authority);

            User user = User.builder()
                    .name(request.getName())
                    .pwd(passwordEncoder.encode(request.getPwd()))
                    .authorities(Collections.singleton(authority))
                    .build();
            userRepository.save(user);
            return request;
        } else throw new RuntimeException("중복 사용자 이름");
    }

    @Transactional
    public LoginResponse signinUser(LoginRequest request) {
        User user = userRepository.findByName(request.getName()).orElseThrow(() -> new RuntimeException("노 유저"));
        if (!passwordEncoder.matches(request.getPwd(), user.getPwd()))
            throw new PasswordExpiredException();

        String id = user.getId().toString();
        String password = user.getPwd();

        List<Authority> authorities = new ArrayList<>();

        Authentication authentication = new UsernamePasswordAuthenticationToken(id, password, authorities);
        String access = tokenProvider.generateAccessToken(authentication);
        String refresh = tokenProvider.generateRefreshToken(authentication);
        return new LoginResponse(access, refresh);
    }
}
