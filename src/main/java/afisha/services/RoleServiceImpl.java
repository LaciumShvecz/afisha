package afisha.services;

import org.springframework.stereotype.Service;
import afisha.models.Role;
import afisha.repositories.RoleRepository;

import java.util.List;

@Service
public class RoleServiceImpl {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }


    public Role getByName(String name) {
        return roleRepository.findByName(name);
    }
}
