package ai.cresta.mappers;

import ai.cresta.entity.User;
import ai.cresta.data.UserDto;

import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User User);

    User userDtoToUser(UserDto UserDto);

    List<UserDto> userListToUserDtoList(List<User> UserList);

    List<User> userDtoListToUserList(List<UserDto> UserDtoList);
    
}
