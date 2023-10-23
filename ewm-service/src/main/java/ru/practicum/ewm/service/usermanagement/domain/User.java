package ru.practicum.ewm.service.usermanagement.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        if (this.id == null || other.id == null) return false;
        return this.id.equals(other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(email, other.email);
    }

    @Override
    public int hashCode() {
        return User.class.hashCode();
    }
}
