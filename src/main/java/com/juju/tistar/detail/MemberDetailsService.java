package com.juju.tistar.detail;

import com.juju.tistar.exception.HttpException;
import com.juju.tistar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public MemberDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return new MemberDetails(
                userRepository.findById(Long.parseLong(id)).orElseThrow(() ->
                        new HttpException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다.")
                )
        );
    }
}
