package ru.practicum.ewm.service.usermanagement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.usermanagement.dto.NewUserRequest;
import ru.practicum.ewm.service.usermanagement.dto.UserDto;
import ru.practicum.ewm.service.usermanagement.service.AdminUserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminService;

    @GetMapping("/admin/users")
    public List<UserDto> getAllUsers(@RequestParam(value = "ids", required = false) List<Long> ids,
                                     @RequestParam(value = "from", defaultValue = "0") int from,
                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Received admin request to GET all users");
        return adminService.getAllUsers(ids, from, size);
    }

    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid NewUserRequest dto) {
        log.info("Received admin POST request to create new user: {}", dto);
        return adminService.createUser(dto);
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("userId") Long userId) {
        log.info("Received admin request to DELETE user with ID={}", userId);
        adminService.deleteUser(userId);
    }
}
