package urfu.test_kurs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import urfu.test_kurs.entity.UserActionLog;
import urfu.test_kurs.repository.UserActionLogRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserActionLogService {
    private final UserActionLogRepository userActionLogRepository;

    @Autowired
    public UserActionLogService(UserActionLogRepository userActionLogRepository) {
        this.userActionLogRepository = userActionLogRepository;
    }

    public List<UserActionLog> getAllUserLogs() {
        return userActionLogRepository.findAll(); // Assuming findAll() method is available in UserActionLogRepository
    }
    public void logAction(String username, String action) {
        UserActionLog log = new UserActionLog();
        log.setUsername(username);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        userActionLogRepository.save(log);
    }
}