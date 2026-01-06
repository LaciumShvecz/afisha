package afisha.services;

import afisha.models.User;
import afisha.models.Role;
import afisha.repositories.RoleRepository;
import afisha.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        System.out.println("Начало регистрации пользователя: " + user.getUsername());

        // Проверка уникальности username
        if (userRepository.existsByUsername(user.getUsername())) {
            System.out.println("Ошибка: Пользователь с таким именем уже существует");
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        // Проверка уникальности email
        if (userRepository.existsByEmail(user.getEmail())) {
            System.out.println("Ошибка: Пользователь с таким email уже существует");
            throw new RuntimeException("Пользователь с таким email уже существует");
        }


        // Кодируем пароль
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        System.out.println("Пароль закодирован");

        // Устанавливаем роль USER по умолчанию
        Role userRole = roleRepository.findByName("ROLE_USER");

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        System.out.println("Роли установлены для пользователя");

        // Сохраняем пользователя
        try {
            User savedUser = userRepository.save(user);
            System.out.println("Пользователь сохранен в БД с ID: " + savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении пользователя: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Ошибка при сохранении пользователя: " + e.getMessage());
        }
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}