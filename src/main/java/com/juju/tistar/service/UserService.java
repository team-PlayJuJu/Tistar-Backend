package com.juju.tistar.service;

import com.juju.tistar.config.TokenProvider;
import com.juju.tistar.entity.Post;
import com.juju.tistar.entity.User;
import com.juju.tistar.entity.enums.Role;
import com.juju.tistar.entity.enums.TokenType;
import com.juju.tistar.exception.HttpException;
import com.juju.tistar.repository.UserRepository;
import com.juju.tistar.request.LoginRequest;
import com.juju.tistar.request.SignupRequest;
import com.juju.tistar.response.LoginResponse;
import com.juju.tistar.response.UserPageResponse;
import com.juju.tistar.response.UserPostsResponse;
import com.juju.tistar.response.RefreshTokenResponse;
import com.mysql.cj.exceptions.PasswordExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;


    @Transactional
    public void signupUser(SignupRequest request) {
        if(userRepository.existsUserByName(request.getName()))
            throw new HttpException(HttpStatus.BAD_REQUEST, "이미 해당 이름을 사용하는 멤버가 존재합니다.");
        User user = User.builder()
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPwd()))
                .roles(List.of(Role.ROLE_USER))
                .build();
        userRepository.save(user);
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
        return userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));
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

    public UserPageResponse getUserPage(final String name, final Pageable pageable) {
        System.out.println(name);
        final User user = userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        String introduction = user.getIntroduction();
        Long userId = user.getId();
        final Slice<Post> myPosts = userRepository.findAllMyPosts(userId, pageable);
        List<UserPostsResponse> postsResponses = myPosts.stream()
                .map(post -> {
                    String imageUrl = post.getImages().get(0).getImagePath();
                    return new UserPostsResponse(
                            post.getId(),
                            imageUrl,
                            post.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());
        return new UserPageResponse(name, introduction, postsResponses);
    }


}
