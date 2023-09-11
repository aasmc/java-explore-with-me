package ru.practicum.ewm.service.usermanagement.service;

import ru.practicum.ewm.service.usermanagement.dto.NewUserRequest;
import ru.practicum.ewm.service.usermanagement.dto.UserDto;

import java.util.List;

public interface AdminUserService {

    List<UserDto> getAllUsers(List<Long> ids, int from, int size);

    UserDto createUser(NewUserRequest dto);

    void deleteUser(Long userId);

}
