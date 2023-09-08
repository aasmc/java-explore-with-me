package ru.practicum.ewm.service.events.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.usermanagement.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomEventsRepositoryImpl implements CustomEventsRepository {

    private final EntityManager em;

    @Override
    public List<Event> findAllEventsBy(List<Long> users,
                                       List<EventState> states,
                                       List<Long> categories,
                                       LocalDateTime start,
                                       LocalDateTime end,
                                       int from,
                                       int size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        if (null != users && !users.isEmpty()) {
            Path<User> user = root.get("user");
            predicates.add(user.get("id").in(users));
        }
        if (null != states && !states.isEmpty()) {
            predicates.add(root.get("state").in(states));
        }
        if (null != categories && !categories.isEmpty()) {
            Path<Category> category = root.get("category");
            predicates.add(category.get("id").in(categories));
        }
        if (null != start && null != end) {
            predicates.add(cb.between(root.get("eventDate"), start, end));
        }
        query.where(predicates.toArray(new Predicate[0]));
        CriteriaQuery<Event> select = query.select(root);
        TypedQuery<Event> typedQuery = em.createQuery(select);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }
}
