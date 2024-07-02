package com.juju.tistar.service;

import com.juju.tistar.config.TokenProvider;
import com.juju.tistar.entity.User;
import com.juju.tistar.entity.enums.Role;
import com.juju.tistar.repository.UserRepository;
import com.juju.tistar.request.LoginRequest;
import com.juju.tistar.response.LoginResponse;
import com.mysql.cj.exceptions.PasswordExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PublicKey;
import java.util.List;

@Slf4j
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
                    .password(passwordEncoder.encode(request.getPwd()))
                    .role(Role.ROLE_USER.getAuthority())
                    .build();
            userRepository.save(user);
        } else throw new RuntimeException("중복 사용자 이름");
    }

    @Transactional
    public LoginResponse signinUser(LoginRequest request) {
        User user = userRepository.findByName(request.getName()).orElseThrow(() -> new RuntimeException("노 유저"));
        if (!passwordEncoder.matches(request.getPwd(), user.getPassword()))
            throw new PasswordExpiredException();
        String id = user.getId().toString();
        String password = user.getPassword();
        Role role = Role.valueOf(user.getRole());
        Authentication authentication = new UsernamePasswordAuthenticationToken(id, password, List.of(role));
        String access = tokenProvider.generateAccessToken(authentication);
        return new LoginResponse(access);
    }

    @Transactional
    public User getCurrentUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info(userId);
        return userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
