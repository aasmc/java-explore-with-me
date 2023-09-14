package ru.practicum.ewm.service.events.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.domain.Category_;
import ru.practicum.ewm.service.events.domain.*;
import ru.practicum.ewm.service.events.dto.EventSort;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.locations.domain.Location;
import ru.practicum.ewm.service.usermanagement.domain.User;
import ru.practicum.ewm.service.usermanagement.domain.User_;

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
        addUsersPredicate(users, root, predicates);
        addStatesPredicate(states, root, predicates);
        addCategoriesPredicate(categories, root, predicates);
        addDatesBetweenPredicate(start, end, cb, root, predicates);
        query.where(predicates.toArray(new Predicate[0]));
        CriteriaQuery<Event> select = query.select(root);
        TypedQuery<Event> typedQuery = em.createQuery(select);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

    @Override
    public List<EventShort> findAllEventsByLocation(Location location,
                                                    String text,
                                                    List<Long> categories,
                                                    Boolean paid,
                                                    LocalDateTime start,
                                                    LocalDateTime end,
                                                    EventSort sort,
                                                    int from,
                                                    int size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EventShort> query = cb.createQuery(EventShort.class);
        Root<Event> root = query.from(Event.class);
        List<Predicate> predicates = createCommonPredicates(cb, root, text, categories, paid, start, end);
        addLocationPredicate(cb, root, location.getLat(), location.getLon(), location.getRadius(), predicates);
        return performQuery(predicates, cb, root, query, sort, from, size);
    }

    @Override
    public List<EventShort> findAllEventsByCoordinates(float lat,
                                                       float lon,
                                                       float radius,
                                                       String text,
                                                       List<Long> categories,
                                                       Boolean paid,
                                                       LocalDateTime start,
                                                       LocalDateTime end,
                                                       EventSort sort,
                                                       int from,
                                                       int size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EventShort> query = cb.createQuery(EventShort.class);
        Root<Event> root = query.from(Event.class);
        List<Predicate> predicates = createCommonPredicates(cb, root, text, categories, paid, start, end);
        addLocationPredicate(cb, root, lat, lon, radius, predicates);
        return performQuery(predicates, cb, root, query, sort, from, size);
    }

    @Override
    public List<EventShort> findAllShortEventsBy(String text,
                                                 List<Long> categories,
                                                 Boolean paid,
                                                 LocalDateTime start,
                                                 LocalDateTime end,
                                                 EventSort sort,
                                                 int from,
                                                 int size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EventShort> query = cb.createQuery(EventShort.class);
        Root<Event> root = query.from(Event.class);
        List<Predicate> predicates = createCommonPredicates(cb, root, text, categories, paid, start, end);
        return performQuery(predicates, cb, root, query, sort, from, size);
    }

    private List<EventShort> performQuery(List<Predicate> predicates,
                                          CriteriaBuilder cb,
                                          Root<Event> root,
                                          CriteriaQuery<EventShort> query,
                                          EventSort sort,
                                          int from,
                                          int size) {
        addSort(sort, cb, query, root);

        query.where(predicates.toArray(new Predicate[0]));
        Path<User> user = root.get(Event_.user);
        CriteriaQuery<EventShort> select = query.select(cb.construct(EventShort.class,
                root.get(Event_.id),
                root.get(Event_.annotation),
                root.get(Event_.category),
                root.get(Event_.eventDate),
                user.get(User_.id),
                user.get(User_.name),
                root.get(Event_.paid),
                root.get(Event_.title),
                root.get(Event_.participationLimit),
                root.get(Event_.publishedOn)));
        TypedQuery<EventShort> typedQuery = em.createQuery(select);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

    private List<Predicate> createCommonPredicates(CriteriaBuilder cb,
                                                   Root<Event> root,
                                                   String text,
                                                   List<Long> categories,
                                                   Boolean paid,
                                                   LocalDateTime start,
                                                   LocalDateTime end) {
        List<Predicate> predicates = new ArrayList<>();
        addPublishedPredicate(cb, root, predicates);
        addTextPredicate(cb, root, predicates, text);
        addCategoriesPredicate(categories, root, predicates);
        addDatesPredicate(cb, root, predicates, start, end);
        addPaidPredicate(cb, root, predicates, paid);
        return predicates;
    }

    private void addLocationPredicate(CriteriaBuilder cb,
                                      Root<Event> root,
                                      float lat,
                                      float lon,
                                      float radius,
                                      List<Predicate> predicates) {
        Path<EventLocation> eventLoc = root.get(Event_.location);
        Predicate distancePredicate = cb.lessThanOrEqualTo(cb.function("distance", Float.class,
                        eventLoc.get(EventLocation_.lat),
                        eventLoc.get(EventLocation_.lon),
                        cb.literal(lat),
                        cb.literal(lon)),
                radius);
        predicates.add(distancePredicate);
    }

    private void addSort(EventSort sort, CriteriaBuilder cb, CriteriaQuery<EventShort> query, Root<Event> root) {
        if (sort == EventSort.EVENT_DATE) {
            query.orderBy(cb.asc(root.get(Event_.eventDate)));
        }
    }

    private void addPaidPredicate(CriteriaBuilder cb,
                                  Root<Event> root,
                                  List<Predicate> predicates,
                                  Boolean paid) {
        if (null != paid) {
            predicates.add(cb.equal(root.get(Event_.paid), paid));
        }
    }

    private void addDatesPredicate(CriteriaBuilder cb,
                                   Root<Event> root,
                                   List<Predicate> predicates,
                                   LocalDateTime start,
                                   LocalDateTime end) {
        if (null != start && null != end) {
            addDatesBetweenPredicate(start, end, cb, root, predicates);
        } else {
            predicates.add(cb.greaterThan(root.get(Event_.eventDate), LocalDateTime.now()));
        }
    }

    private void addTextPredicate(CriteriaBuilder cb,
                                  Root<Event> root,
                                  List<Predicate> predicates,
                                  String text) {
        if (text != null) {
            String pattern = "%" + text.toLowerCase() + "%";
            Predicate likePredicate = cb.or(
                    cb.like(
                            cb.lower(
                                    root.get(Event_.annotation)
                            ),
                            pattern
                    ),
                    cb.like(
                            cb.lower(
                                    root.get(Event_.description)
                            ),
                            pattern
                    )
            );
            predicates.add(likePredicate);
        }
    }

    private void addPublishedPredicate(CriteriaBuilder cb,
                                       Root<Event> root,
                                       List<Predicate> predicates) {
        predicates.add(cb.equal(root.get(Event_.state), EventState.PUBLISHED));
    }

    private void addDatesBetweenPredicate(LocalDateTime start, LocalDateTime end, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (null != start && null != end) {
            predicates.add(cb.between(root.get(Event_.eventDate), start, end));
        }
    }

    private void addCategoriesPredicate(List<Long> categories, Root<Event> root, List<Predicate> predicates) {
        if (null != categories && !categories.isEmpty()) {
            Path<Category> category = root.get(Event_.category);
            predicates.add(category.get(Category_.id).in(categories));
        }
    }

    private void addStatesPredicate(List<EventState> states, Root<Event> root, List<Predicate> predicates) {
        if (null != states && !states.isEmpty()) {
            predicates.add(root.get(Event_.state).in(states));
        }
    }

    private void addUsersPredicate(List<Long> users, Root<Event> root, List<Predicate> predicates) {
        if (null != users && !users.isEmpty()) {
            Path<User> user = root.get(Event_.user);
            predicates.add(user.get(User_.id).in(users));
        }
    }
}
