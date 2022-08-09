package lord.dev.repository;

import lord.dev.model.Role;
import lord.dev.model.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(ERole name);
}
