package com.juju.tistar.detail;

import com.juju.tistar.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
@AllArgsConstructor
public class MemberDetails implements UserDetails {
    private User user;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getPermission())
        ).toList();
    }

    @Override
    @Deprecated
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.getName();
    }
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
