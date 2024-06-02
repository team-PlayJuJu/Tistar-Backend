package com.juju.tistar.service;

import com.juju.tistar.entity.Authority;
import com.juju.tistar.entity.User;
import com.juju.tistar.repository.AuthorityRepository;
import com.juju.tistar.repository.UserRepository;
import com.juju.tistar.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public SignupRequest signupUser(SignupRequest request) {
        if (userRepository.findByName(request.getName()) != null) {
            throw new IllegalArgumentException("이미 사용 중인 이름입니다.");
        }
        Authority authority = Authority.builder()
                .name("ROLE_USER")
                .build();

        User user = User.builder()
                .name(request.getName())
                .pwd(passwordEncoder.encode(request.getPwd()))
                .authorities(Collections.singleton(authority))
                .build();
        return SignupRequest.from(userRepository.save(user));
    }

//    @Transactional
//    public User SigninUser(LoginRequest loginRequest) {
//
//    }


}
