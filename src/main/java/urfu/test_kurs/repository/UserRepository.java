package urfu.test_kurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import urfu.test_kurs.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}