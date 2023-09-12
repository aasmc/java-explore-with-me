package ru.practicum.ewm.service.error;

public final class ErrorConstants {

    private ErrorConstants() {

    }

    public static final String NOT_FOUND_REASON = "The required object was not found.";
    public static final String BAD_REQUEST_REASON = "Incorrectly made request.";
    public static final String UNKNOWN_REASON = "Unknown reason";
    public static final String DATA_INTEGRITY_VIOLATION_REASON = "Integrity constraint has been violated.";
    public static final String CONDITIONS_NOT_MET_REASON = "For the requested operation the conditions are not met.";
    public static final String FAILED_STATISTICS_REASON = "Error occurred while trying to retrieve statistics from Statistics Client.";
    public static final String CATEGORY_NOT_FOUND_MSG = "Category with id=%d was not found";
    public static final String USER_NOT_FOUND_MSG = "User with id=%d was not found";
    public static final String LOCATION_NOT_FOUND_MSG = "Location with id=%d was not found";
    public static final String COMPILATION_NOT_FOUND_MSG = "Compilation with id=%d was not found";
    public static final String REQUEST_NOT_FOUND_MSG = "Request with id=%d was not found";
    public static final String EVENT_INITIATOR_SELF_REQUEST_MSG = "User with id=%d is initiator if event with id=%d and cannot request to take part in it.";
    public static final String EVENT_NOT_PUBLISHED_MSG = "Cannot create request to take part in event with id=%d because it has not been published yet.";
    public static final String EVENT_NOT_FOUND_MSG = "Event with id=%d was not found";
    public static final String EVENT_WRONG_STATE_MSG = "Cannot %s the event because it's not in the right state: %s";
    public static final String EVENT_WRONG_UPDATE_DATE_MSG = "Cannot update the event because new date: %s is less than 1 hour before publish date.";
    public static final String EVENT_NEW_DATE_WRONG_MSG = "Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s";
    public static final String EVENT_REQUEST_UPDATE_NOT_ALLOWED_MSG = "Cannot update requests, because some of them are not in PENDING state.";
    public static final String EVENT_PARTICIPATION_LIMIT_REACHED = "The participant limit has been reached";
    public static final String COMPILATION_CREATION_FAILED_EVENTS_NOT_FOUND = "Failed to create a compilation because some of the events were not found.";

}
