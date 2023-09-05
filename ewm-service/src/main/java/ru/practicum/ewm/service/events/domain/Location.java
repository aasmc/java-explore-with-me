package ru.practicum.ewm.service.events.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Location {
    private Float lat;
    private Float lon;
}
