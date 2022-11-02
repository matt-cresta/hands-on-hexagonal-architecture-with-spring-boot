package ai.cresta.service;

import ai.cresta.data.UserDto;
import ai.cresta.ports.api.UserServicePort;
import ai.cresta.ports.spi.UserPersistencePort;

import java.util.List;

public class UserServiceImpl implements UserServicePort {

    private UserPersistencePort userPersistencePort;

    public UserServiceImpl(UserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        return userPersistencePort.addUser(userDto);
    }

    @Override
    public void deleteUserById(Long id) {
        userPersistencePort.deleteUserById(id);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        return userPersistencePort.updateUser(userDto);
    }

    @Override
    public List<UserDto> getUsers() {
        return userPersistencePort.getUsers();
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userPersistencePort.getUserById(userId);
    }
}