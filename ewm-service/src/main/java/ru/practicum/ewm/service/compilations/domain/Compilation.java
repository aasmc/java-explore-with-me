package ru.practicum.ewm.service.compilations.domain;

import lombok.*;
import ru.practicum.ewm.service.events.domain.Event;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name = "COMPILATIONS")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "compilation")
    private Set<Event> events = new HashSet<>();
    private Boolean pinned;
    private String title;
}
