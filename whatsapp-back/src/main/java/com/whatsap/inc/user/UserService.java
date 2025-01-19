package com.whatsap.inc.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;;

    public List<UserResponse> findAllUsersExceptSelf(Authentication currentUser) {
        final String publicId = currentUser.getName();
        return userRepository.findAllUsersExceptSelf(publicId).stream()
                .map(UserMapper::toUserResponse).toList();
    }
}
