package se.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.db.model.Password;
import se.db.model.Role;
import se.db.model.User;
import se.db.repository.PasswordRepository;
import se.db.repository.RoleRepository;
import se.db.repository.UserRepository;

import java.util.Set;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserDetailsService() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String mail) {

        User user = userRepository.findByMail(mail);
        Password password = passwordRepository.findById(user.getPasswordId());
        Role role = roleRepository.findById(user.getRoleId());
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
        if (user == null) {
            throw new UsernameNotFoundException(mail);
        }
        CustomUserDetails usr1 = new CustomUserDetails(user, passwordEncoder.encode(password.getHashedPassword()), authorities);

        return usr1;
    }
}
