package be.ucll.project.userservice.web;

import be.ucll.project.userservice.api.UserApiDelegate;
import be.ucll.project.userservice.api.model.ApiUser;
import be.ucll.project.userservice.api.model.ApiUsers;
import be.ucll.project.userservice.domain.User;
import be.ucll.project.userservice.domain.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
public class UserController implements UserApiDelegate {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Override
    public ResponseEntity<ApiUsers> getUsers() {
        ApiUsers users = new ApiUsers();
        users.setUsers(
                userService.getUsers().stream()
                        .map(this::toDto)
                        .toList()
        );

        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<ApiUser>createUser(ApiUser user) {

        return ResponseEntity.ok(toDto(userService.createUser(user)));
    }

    private ApiUser toDto(User user) {
        return new ApiUser()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail());
    }


}
