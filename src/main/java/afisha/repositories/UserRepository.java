package afisha.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import afisha.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}