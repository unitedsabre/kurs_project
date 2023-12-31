package urfu.test_kurs.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import urfu.test_kurs.service.UserActionLogService;
import org.springframework.stereotype.Component;

@Component
public class UserLoginLogoutEventListener {
    @Autowired
    private UserActionLogService userActionLogService;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        userActionLogService.logAction(username, "Вход пользователя");
    }

    @EventListener
    public void onLogoutSuccess(LogoutSuccessEvent event) {
        String username = event.getAuthentication().getName();
        userActionLogService.logAction(username, "Выход пользователя");
    }
}
