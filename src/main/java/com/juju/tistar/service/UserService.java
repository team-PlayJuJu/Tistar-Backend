package com.juju.tistar.service;

import com.juju.tistar.config.TokenProvider;
import com.juju.tistar.entity.User;
import com.juju.tistar.entity.enums.Role;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;


    @Transactional
    public void signupUser(LoginRequest request) {
        if(!userRepository.existsUserByName(request.getName())) {
            User user = User.builder()
                    .name(request.getName())
                    .pwd(passwordEncoder.encode(request.getPwd()))
                    .roles(List.of(Role.USER))
                    .build();
            userRepository.save(user);
        } else throw new RuntimeException("중복 사용자 이름");
    }

    @Transactional
    public LoginResponse signinUser(LoginRequest request) {
        User user = userRepository.findByName(request.getName()).orElseThrow(() -> new RuntimeException("노 유저"));
        if (!passwordEncoder.matches(request.getPwd(), user.getPwd()))
            throw new PasswordExpiredException();

        String id = user.getId().toString();
        String password = user.getPwd();

        List<Role> authorities = user.getRoles();

        Authentication authentication = new UsernamePasswordAuthenticationToken(id, password, authorities);
        String access = tokenProvider.generateAccessToken(authentication);
        String refresh = tokenProvider.generateRefreshToken(authentication);
        return new LoginResponse(access, refresh);
    }
}
