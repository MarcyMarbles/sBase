package kz.saya.sbasesecurity.Service;

import kz.saya.sbasecore.Entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> {
                    String roleName = role.getName();
                    if (!roleName.startsWith("ROLE_")) {
                        roleName = "ROLE_" + roleName;
                    }
                    return new SimpleGrantedAuthority(roleName);
                })
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        OffsetDateTime now = OffsetDateTime.now();
        return user.getStart_date_ts().isBefore(now) && user.getEnd_date_ts().isAfter(now); // Check if the account is active
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // No locking mechanism implemented
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // No expiration mechanism implemented
        // JWT expiration is handled in the token itself
    }

    @Override
    public boolean isEnabled() {
        return true; // No disabling mechanism implemented
    }


}
