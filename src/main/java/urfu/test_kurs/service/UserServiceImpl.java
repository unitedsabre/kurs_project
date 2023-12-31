package urfu.test_kurs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import urfu.test_kurs.dto.UserDto;
import urfu.test_kurs.entity.Role;
import urfu.test_kurs.entity.User;
import urfu.test_kurs.repository.RoleRepository;
import urfu.test_kurs.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role;
        if (userRepository.count() == 0) {
            role = roleRepository.findByName("ADMIN");
            if (role == null) {
                role = createAdminRole();
            }
        } else if (userRepository.count() == 1) {
            role = roleRepository.findByName("USER");
            if (role == null) {
                role = createUserRole();
            }
        } else {
            role = roleRepository.findByName("READ_ONLY");
            if (role == null) {
                role = createReadOnlyRole();
            }
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setName(str[0]);
        userDto.setUsername(user.getUsername());
        return userDto;
    }

    private Role createAdminRole() {
        Role role = new Role();
        role.setName("ADMIN");
        return roleRepository.save(role);
    }

    private Role createReadOnlyRole() {
        Role role = new Role();
        role.setName("READ_ONLY");
        return roleRepository.save(role);
    }

    private Role createUserRole() {
        Role role = new Role();
        role.setName("USER");
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            Role role = roleRepository.findByName(roleName);
            if (role != null) {
                // Remove existing roles for the user to ensure uniqueness
                user.getRoles().clear();
                // Add the new role to the user's roles
                user.getRoles().add(role);
                userRepository.save(user);
            } else {
                // Handle case when role does not exist
            }
        } else {
            // Handle case when user does not exist
        }
    }
}