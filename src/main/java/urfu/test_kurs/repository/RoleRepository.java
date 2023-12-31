package urfu.test_kurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import urfu.test_kurs.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

}