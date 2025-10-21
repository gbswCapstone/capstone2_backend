package Capstone.capstoneProject.security;




import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class
CustomSecurityUserDetails implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomSecurityUserDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // DB에서 사용자 정보 조회 후 Spring Security의 UserDetails 객체로 반환
    // email을 기준으로 분류
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().toString())
                .build();
    }
    Users user;
    public Users getUser() {
        return user;
    }
}
