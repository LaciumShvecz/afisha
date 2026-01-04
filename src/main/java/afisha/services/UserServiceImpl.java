package afisha.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import afisha.models.User;
import afisha.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleServiceImpl roleService;


    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleServiceImpl roleService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void createUser(User user) {
        user.setRoles(user.getRoles().stream()
                .map(role -> roleService.getByName(role.getName()))
                .collect(Collectors.toSet()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long id, User user) {
        User updateUser = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                "There is no user with ID = '" + id + "' in Database"
        ));
        updateUser.setName(user.getName());
        updateUser.setUsername(user.getUsername());
        updateUser.setAge(user.getAge());
        if (!user.getPassword().equals(updateUser.getPassword())) {
            updateUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        updateUser.setRoles(user.getRoles().stream()
                .map(role -> roleService.getByName(role.getName()))
                .collect(Collectors.toSet()));

        userRepository.save(updateUser);
    }


    @Transactional
    public void deleteUserById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }


    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                "There is no user with ID = '" + id + "' in Database"
        ));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}