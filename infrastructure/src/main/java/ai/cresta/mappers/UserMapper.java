package ai.cresta.mappers;

import ai.cresta.data.UserDto;
import ai.cresta.entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User User);

    User userDtoToUser(UserDto UserDto);

    List<UserDto> userListToUserDtoList(List<User> UserList);

    List<User> userDtoListToUserList(List<UserDto> UserDtoList);
    
}
