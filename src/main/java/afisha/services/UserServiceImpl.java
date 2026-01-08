package afisha.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import afisha.models.User;
import afisha.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
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

    // ============ ОСНОВНЫЕ CRUD МЕТОДЫ ============

    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User saveUser(User user) {
        // Если это новый пользователь (без ID) или пароль не закодирован
        if (user.getId() == null || !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Если есть роли, получаем их полные объекты из БД
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            user.setRoles(user.getRoles().stream()
                    .map(role -> roleService.getByName(role.getName()))
                    .collect(Collectors.toSet()));
        }

        return userRepository.save(user);
    }


    @Transactional
    public void updateUser(Long id, User user) {
        User updateUser = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                "There is no user with ID = '" + id + "' in Database"
        ));
        updateUser.setName(user.getName());
        updateUser.setUsername(user.getUsername());
        updateUser.setEmail(user.getEmail());

        // Обновляем пароль только если он изменился и не закодирован
        if (user.getPassword() != null && !user.getPassword().equals(updateUser.getPassword())) {
            if (!user.getPassword().startsWith("$2a$")) { // Проверяем, не закодирован ли уже пароль
                updateUser.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                updateUser.setPassword(user.getPassword());
            }
        }

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            updateUser.setRoles(user.getRoles().stream()
                    .map(role -> roleService.getByName(role.getName()))
                    .collect(Collectors.toSet()));
        }

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

    // ============ ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ============

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void deleteUser(User user) {
        if (user != null && user.getId() != null) {
            deleteUserById(user.getId());
        }
    }

    public boolean userExistsById(Long id) {
        return userRepository.findById(id).isPresent();
    }

    public boolean userExistsByUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Transactional
    public User registerNewUser(User user) {
        // Проверка уникальности username
        if (userExistsByUsername(user.getUsername())) {
            throw new RuntimeException("User with username '" + user.getUsername() + "' already exists");
        }

        // Проверка уникальности email
        if (userExistsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email '" + user.getEmail() + "' already exists");
        }

        // Устанавливаем роль USER по умолчанию, если роли не указаны
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            var defaultRole = roleService.getByName("ROLE_USER");
            if (defaultRole != null) {
                user.setRoles(Set.of(defaultRole));
            }
        }

        // Используем основной метод сохранения
        return saveUser(user);
    }

    @Transactional
    public User updateUserPassword(String username, String newPassword) {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new NoSuchElementException("User with username '" + username + "' not found");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    @Transactional
    public User updateUserEmail(String username, String newEmail) {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new NoSuchElementException("User with username '" + username + "' not found");
        }

        // Проверяем, не используется ли email другим пользователем
        User existingUser = getUserByEmail(newEmail);
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new RuntimeException("Email '" + newEmail + "' is already in use");
        }

        user.setEmail(newEmail);
        return userRepository.save(user);
    }

    @Transactional
    public User updateUserProfile(String username, String name, String email) {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new NoSuchElementException("User with username '" + username + "' not found");
        }

        if (name != null) {
            user.setName(name);
        }

        if (email != null && !email.equals(user.getEmail())) {
            // Проверяем уникальность email
            User existingUser = getUserByEmail(email);
            if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                throw new RuntimeException("Email '" + email + "' is already in use");
            }
            user.setEmail(email);
        }

        return userRepository.save(user);
    }

    public List<User> searchUsers(String keyword) {
        // Ищем по имени, username или email
        return userRepository.findAll().stream()
                .filter(user ->
                        (user.getName() != null && user.getName().toLowerCase().contains(keyword.toLowerCase())) ||
                                (user.getUsername() != null && user.getUsername().toLowerCase().contains(keyword.toLowerCase())) ||
                                (user.getEmail() != null && user.getEmail().toLowerCase().contains(keyword.toLowerCase()))
                )
                .collect(Collectors.toList());
    }

    public long countUsers() {
        return userRepository.count();
    }

    // Метод для проверки пароля
    public boolean checkPassword(String username, String rawPassword) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // Метод для смены пароля с проверкой старого
    @Transactional
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }

        // Проверяем старый пароль
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        // Устанавливаем новый пароль
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }
}