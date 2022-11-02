package ai.cresta.ports.api;

import ai.cresta.data.UserDto;

import java.util.List;

public interface UserServicePort {
    UserDto addUser(UserDto userDto);

    void deleteUserById(Long id);

    UserDto updateUser(UserDto userDto);

    List<UserDto> getUsers();

    UserDto getUserById(Long userId);
}
