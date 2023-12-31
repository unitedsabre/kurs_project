package urfu.test_kurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import urfu.test_kurs.entity.UserActionLog;

public interface UserActionLogRepository extends JpaRepository<UserActionLog, Long> {
}
