package ru.practicum.ewm.service.usermanagement.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class NewUserRequest {
    @Size(min = 6, max = 254)
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}
