package ru.practicum.ewm.service.usermanagement.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.usermanagement.domain.User;
import ru.practicum.ewm.service.usermanagement.dto.NewUserRequest;
import ru.practicum.ewm.service.usermanagement.dto.UserDto;
import ru.practicum.ewm.service.usermanagement.dto.UserShortDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User mapToDomain(NewUserRequest dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public UserDto mapToDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }

    public UserShortDto mapToShortDto(User entity) {
        return UserShortDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public List<UserDto> mapToDtoList(List<User> users) {
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

}
