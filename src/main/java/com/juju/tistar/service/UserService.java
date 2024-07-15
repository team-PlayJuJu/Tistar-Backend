package com.juju.tistar.service;

import com.juju.tistar.config.TokenProvider;
import com.juju.tistar.entity.User;
import com.juju.tistar.entity.enums.Role;
import com.juju.tistar.entity.enums.TokenType;
import com.juju.tistar.exception.HttpException;
import com.juju.tistar.repository.UserRepository;
import com.juju.tistar.request.LoginRequest;
import com.juju.tistar.request.SignupRequest;
import com.juju.tistar.response.LoginResponse;
import com.juju.tistar.response.RefreshTokenResponse;
import com.mysql.cj.exceptions.PasswordExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;


    @Transactional
    public void signupUser(SignupRequest request) {
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
        return tokenProvider.generateTokenSet(user.getId());
    }

    @Transactional
    public User getCurrentUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info(userId);
        return userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public RefreshTokenResponse refreshToken(String access, String refresh){
        access = access.substring(7);
        refresh = refresh.substring(7);

        Boolean validateAccess = tokenProvider.validateToken(access);
        Boolean validateRefresh = tokenProvider.validateToken(refresh);

        User user = userRepository.findById(
                Long.parseLong(tokenProvider.getClaims(access).getSubject())
        ).orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "해당하는 유저를 찾을 수 없습니다."));

        if(validateAccess && validateRefresh) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "엑세스 토큰과 리프레스 토큰이 모두 만료되었습니다.");
        } else if(validateAccess) {
            return new RefreshTokenResponse(
                    tokenProvider.generateToken(user.getId(), TokenType.ACCESS),
                    refresh
            );
        } else if(validateRefresh) {
            return new RefreshTokenResponse(
                    access,
                    tokenProvider.generateToken(user.getId(), TokenType.REFRESH)
            );
        } else {
            throw new HttpException(HttpStatus.BAD_REQUEST, "만료된 토큰이 없습니다");
        }
    }
}
