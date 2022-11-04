package ai.cresta.adapters;

import ai.cresta.data.UserDto;
import ai.cresta.entity.User;
import ai.cresta.mappers.UserMapper;
import ai.cresta.ports.spi.UserPersistencePort;
import ai.cresta.repository.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserJpaAdapter implements UserPersistencePort {

    @Autowired 
    private UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto UserDto) {
        User user = UserMapper.INSTANCE.userDtoToUser(UserDto);

        User userSaved = userRepository.save(user);

        return UserMapper.INSTANCE.userToUserDto(userSaved);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
        
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        return addUser(userDto);
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> userList = userRepository.findAll();

        return UserMapper.INSTANCE.userListToUserDtoList(userList);
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return UserMapper.INSTANCE.userToUserDto(user.get());
        }

        return null;
    }
    
}
