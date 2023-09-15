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
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "compilation")
    private Set<Event> events = new HashSet<>();
    @Column(name = "pinned")
    private Boolean pinned;
    @Column(name = "title")
    private String title;
}
