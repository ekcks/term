package com.bips.reserve.service.user;

import com.bips.reserve.domain.entity.User;
        import org.springframework.security.core.userdetails.UserDetailsService;

        import java.util.Optional;

public interface UserService extends UserDetailsService {
    User createUser(User user);
    User getUserByEmail(String email);
}