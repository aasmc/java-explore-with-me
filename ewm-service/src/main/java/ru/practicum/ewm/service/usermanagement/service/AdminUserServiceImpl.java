package ru.practicum.ewm.service.usermanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.usermanagement.domain.User;
import ru.practicum.ewm.service.usermanagement.dto.NewUserRequest;
import ru.practicum.ewm.service.usermanagement.dto.UserDto;
import ru.practicum.ewm.service.usermanagement.mapper.UserMapper;
import ru.practicum.ewm.service.usermanagement.repository.UsersRepository;
import ru.practicum.ewm.service.util.OffsetBasedPageRequest;

import java.util.List;

import static ru.practicum.ewm.service.error.ErrorConstants.USER_NOT_FOUND_MSG;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UsersRepository usersRepository;
    private final UserMapper mapper;

    @Override
    public List<UserDto> getAllUsers(List<Long> ids, int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size);
        List<User> allUsers = usersRepository.findAllUsers(ids, pageable);
        return mapper.mapToDtoList(allUsers);
    }

    @Override
    public UserDto createUser(NewUserRequest dto) {
        try {
            User user = mapper.mapToDomain(dto);
            return mapper.mapToDto(usersRepository.saveAndFlush(user));
        } catch (DataIntegrityViolationException ex) {
            throw EwmServiceException.dataIntegrityException(ex.getMessage());
        }
    }

    @Override
    public void deleteUser(Long userId) {
        usersRepository.findById(userId)
                .ifPresentOrElse(user -> usersRepository.deleteById(userId),
                        () -> {
                            String msg = String.format(USER_NOT_FOUND_MSG, userId);
                            throw EwmServiceException.notFoundException(msg);
                        });
    }
}
