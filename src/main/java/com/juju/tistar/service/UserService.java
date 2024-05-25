package com.juju.tistar.service;

import com.juju.tistar.entity.User;
import com.juju.tistar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public User signupUser(User user) {
        String hashedPwd = passwordEncoder.encode(user.getPwd());
        user.setPwd(hashedPwd);
        user.setCreateDt(String.valueOf(new Date(System.currentTimeMillis())));
        return userRepository.save(user);
    }
}
