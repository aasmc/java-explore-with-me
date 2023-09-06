package ru.practicum.ewm.service.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.service.usermanagement.domain.User;

public interface UsersRepository extends JpaRepository<User, Long> {
}
