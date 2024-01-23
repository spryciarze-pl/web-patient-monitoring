package se.secuirty;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import se.db.model.User;

import java.util.Collection;
import java.util.Set;

@Getter
public class CustomUserDetails implements UserDetails {

    private User user;
    private String password;
    private Set<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return user.getMail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public CustomUserDetails(User user, String password, Set<GrantedAuthority> grantedAuthorities) {
        this.user = user;
        this.password = password;
        this.authorities = grantedAuthorities;
    }

}
