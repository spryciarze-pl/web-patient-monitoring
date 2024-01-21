package se.secuirty;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import se.db.model.Clinic;
import se.db.model.Specialization;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

public class CustomUserDetails implements UserDetails {

    private Integer id;
    private String name;
    private String surname;
    private String pin;
    private String phone;
    private String mail;
    private LocalDateTime registrationTime;
    private boolean confirmed;
    private String password;
    private Integer roleId;
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
        return mail;
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
        return true;
    }
}
