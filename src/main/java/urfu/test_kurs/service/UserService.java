package urfu.test_kurs.service;



import urfu.test_kurs.dto.UserDto;
import urfu.test_kurs.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByUsername(String username);

    List<UserDto> findAllUsers();

    void addRoleToUser(String username, String roleName);

}
