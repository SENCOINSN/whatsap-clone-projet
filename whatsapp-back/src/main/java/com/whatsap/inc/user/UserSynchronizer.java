package com.whatsap.inc.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSynchronizer {
    private final UserRepository userRepository;

    public void syncUser(Jwt token) {
      getUserEmailOnToken(token).ifPresent(email ->{
                  log.info("User email: {}", email);
                  Optional<User> optUser =
                          userRepository.findByEmail(email);
          User user = UserMapper.fromTokenAttributes(token.getClaims());
          optUser.ifPresent(value ->
              user.setId(value.getId())
          );
          userRepository.save(user);
      });

    }

    private Optional<String> getUserEmailOnToken(Jwt token) {
        Map<String, Object> claims = token.getClaims(); // <1>
        if(claims.containsKey("email")) { // <2>
            return Optional.of(claims.get("email").toString()); // <3>
        }
        return Optional.empty();
    }



}
