package ru.practicum.ewm.service.usermanagement.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.service.usermanagement.domain.User;

import java.util.List;

public interface UsersRepository extends JpaRepository<User, Long> {

    @Query("select u from User u " +
            "where (:ids) is null or u.id in :ids")
    List<User> findAllUsers(@Param("ids") List<Long> ids, Pageable pageable);

}
