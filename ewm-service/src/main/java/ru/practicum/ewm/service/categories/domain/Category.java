package ru.practicum.ewm.service.categories.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CATEGORIES")
@ToString
@Builder
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Category)) return false;
        Category other = (Category) obj;
        if (this.id == null || other.id == null) return false;
        return this.id.equals(other.id) && Objects.equals(this.name, other.name);
    }

    @Override
    public int hashCode() {
        return Category.class.hashCode();
    }
}
