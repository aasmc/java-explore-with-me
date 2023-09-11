package ru.practicum.ewm.service.requests.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.requests.dto.ParticipationStatus;
import ru.practicum.ewm.service.usermanagement.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "REQUESTS")
@Entity
@Builder
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    private Event event;
    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester", nullable = false)
    private User requester;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ParticipationStatus status;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Request)) return false;
        Request other = (Request) obj;
        if (this.id == null || other.id == null) return false;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Request.class.hashCode();
    }
}
