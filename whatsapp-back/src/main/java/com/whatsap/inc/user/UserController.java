package com.whatsap.inc.user;

import com.whatsap.inc.constants.ApiPath;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.whatsap.inc.constants.ApiPath.BASE_PATH;
import static com.whatsap.inc.constants.ApiPath.USER;

@RestController
@RequestMapping(BASE_PATH+USER)
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {
    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(Authentication authentication) {
        return ResponseEntity.ok(userService.findAllUsersExceptSelf(authentication));
    }

}
