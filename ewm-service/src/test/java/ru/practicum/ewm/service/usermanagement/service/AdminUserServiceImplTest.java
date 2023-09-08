package ru.practicum.ewm.service.usermanagement.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.service.BaseIntegTest;
import ru.practicum.ewm.service.error.ErrorConstants;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.usermanagement.domain.User;
import ru.practicum.ewm.service.usermanagement.dto.NewUserRequest;
import ru.practicum.ewm.service.usermanagement.dto.UserDto;
import ru.practicum.ewm.service.usermanagement.repository.UsersRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.ewm.service.testutil.TestConstants.USER_EMAIL;
import static ru.practicum.ewm.service.testutil.TestConstants.USER_NAME;
import static ru.practicum.ewm.service.testutil.TestData.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class AdminUserServiceImplTest extends BaseIntegTest {

    private final UsersRepository repository;
    private final AdminUserServiceImpl adminUserService;

    @Test
    void getAllUsers_whenHasUsers_returnsCorrectList() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(saveUser(USER_EMAIL + i, USER_NAME + i));
        }
        // ids = {4,5,6,7,8}
        List<Long> ids = users.stream().map(User::getId)
                .skip(3)
                .limit(5)
                .collect(Collectors.toList());
        // take 3 Users with above ids, starting from 3-rd User, i.e
        // take Users with id=7 id=8
        List<UserDto> result = adminUserService.getAllUsers(ids, 3, 3)
                .stream()
                .sorted(Comparator.comparing(UserDto::getId))
                .collect(Collectors.toList());

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(ids.get(3));
        assertThat(result.get(1).getId()).isEqualTo(ids.get(4));
    }

    @Test
    void getAllUsers_whenNoUsers_returnsEmptyList() {
        List<UserDto> allUsers = adminUserService.getAllUsers(null, 0, 10);
        assertThat(allUsers).isEmpty();
    }

    @Test
    void deleteUser_whenNotFound_throws() {
        EwmServiceException ex = assertThrows(EwmServiceException.class,
                () -> adminUserService.deleteUser(100L));
        assertThat(ex.getReason()).isEqualTo(ErrorConstants.NOT_FOUND_REASON);
    }

    @Test
    void deleteUser_whenAllOk_deletesUser() {
        User user = saveUser(USER_EMAIL, USER_NAME);
        adminUserService.deleteUser(user.getId());
        Optional<User> byId = repository.findById(user.getId());
        assertThat(byId).isEmpty();
    }

    @Test
    void createUser_whenEmailNotUnique_throws() {
        User user = saveUser(USER_EMAIL, USER_NAME);
        NewUserRequest dto = newUserRequestFromUser(user);
        EwmServiceException ex = assertThrows(EwmServiceException.class,
                () -> adminUserService.createUser(dto));

        assertThat(ex.getReason()).isEqualTo(ErrorConstants.DATA_INTEGRITY_VIOLATION_REASON);
    }

    @Test
    void createUser_whenAllOk_createsUser() {
        NewUserRequest dto = newUserRequest(USER_EMAIL, USER_NAME);
        UserDto result = adminUserService.createUser(dto);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(dto.getName());
        assertThat(result.getEmail()).isEqualTo(dto.getEmail());
    }

    private User saveUser(String email, String name) {
        User user = transientUser(email, name);
        return repository.saveAndFlush(user);
    }

}