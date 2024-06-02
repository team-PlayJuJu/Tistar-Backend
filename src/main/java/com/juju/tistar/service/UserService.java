package com.juju.tistar.service;

import com.juju.tistar.entity.Authority;
import com.juju.tistar.entity.User;
import com.juju.tistar.repository.AuthorityRepository;
import com.juju.tistar.repository.UserRepository;
import com.juju.tistar.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signupUser(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getPwd() == null || user.getPwd().isEmpty()) {
            throw new IllegalArgumentException("이름 또는 비번이 없음");
        }
        if (userRepository.findByUsername(user.getName()) != null) {
            throw new IllegalArgumentException("이미 사용 중인 이름");
        }
        String hashedPwd = passwordEncoder.encode(user.getPwd());
        user.setPwd(hashedPwd);
        user.setCreateDt(String.valueOf(new Date(System.currentTimeMillis())));
        userRepository.save(user);
        Authority authority = new Authority();
        authority.setName("ROLE_USER");
        authority.setUser(user);
        authorityRepository.save(authority);
        return user;
    }

//    @Transactional
//    public User SigninUser(LoginRequest loginRequest) {
//
//    }


}
