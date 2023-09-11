package ru.practicum.ewm.service.usermanagement.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.service.BaseJpaTest;
import ru.practicum.ewm.service.usermanagement.domain.User;
import ru.practicum.ewm.service.util.OffsetBasedPageRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.ewm.service.testutil.TestConstants.USER_EMAIL;
import static ru.practicum.ewm.service.testutil.TestConstants.USER_NAME;
import static ru.practicum.ewm.service.testutil.TestData.transientUser;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UsersRepositoryTest extends BaseJpaTest {

    private final UsersRepository usersRepository;

    @Test
    void findAllUsers_whenHasUsers_idsIsNull_returnsCorrectListWithCorrectOffset() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(saveUser(USER_EMAIL + i, USER_NAME + i));
        }

        Pageable pageable = new OffsetBasedPageRequest(3, 3);

        List<User> result = usersRepository.findAllUsers(null, pageable)
                .stream()
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(users.get(3).getId());
        assertThat(result.get(1).getId()).isEqualTo(users.get(4).getId());
        assertThat(result.get(2).getId()).isEqualTo(users.get(5).getId());
    }

    @Test
    void findAllUsers_whenHasUsers_returnsCorrectListWithCorrectOffset() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(saveUser(USER_EMAIL + i, USER_NAME + i));
        }
        List<Long> ids = users.stream().map(User::getId)
                .skip(3)
                .limit(5)
                .collect(Collectors.toList());

        Pageable pageable = new OffsetBasedPageRequest(0, 3);

        List<User> result = usersRepository.findAllUsers(ids, pageable)
                .stream()
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(ids.get(0));
        assertThat(result.get(1).getId()).isEqualTo(ids.get(1));
        assertThat(result.get(2).getId()).isEqualTo(ids.get(2));
    }

    private User saveUser(String email, String name) {
        return usersRepository.saveAndFlush(transientUser(email, name));
    }

}