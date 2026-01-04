package afisha.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import afisha.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}