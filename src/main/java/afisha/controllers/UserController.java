package afisha.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import afisha.models.Role;
import afisha.models.User;
import afisha.services.RoleServiceImpl;
import afisha.services.UserServiceImpl;

import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping("/api/profile")
public class UserController {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;
    ;

    @Autowired
    public UserController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        return new ResponseEntity<>(userService.getUserByUsername(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<Collection<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }
}