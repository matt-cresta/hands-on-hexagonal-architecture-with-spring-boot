package ai.cresta.ports.spi;

import ai.cresta.data.UserDto;

import java.util.List;

public interface UserPersistencePort {
    UserDto addUser(UserDto UserDto);

    void deleteUserById(Long id);

    UserDto updateUser(UserDto UserDto);

    List<UserDto> getUsers();

    UserDto getUserById(Long id);
}
