package ru.practicum.ewm.service.error;

public class ErrorConstants {
    public static final String NOT_FOUND_REASON = "The required object was not found.";
    public static final String BAD_REQUEST_REASON = "Incorrectly made request.";
    public static final String UNKNOWN_REASON = "Unknown reason";
    public static final String DATA_INTEGRITY_VIOLATION_REASON = "Integrity constraint has been violated.";
    public static final String CONDITIONS_NOT_MET_REASON = "For the requested operation the conditions are not met.";
    public static final String FAILED_STATISTICS_REASON = "Error occurred while trying to retrieve statistics from Statistics Client.";
    public static final String CATEGORY_NOT_FOUND_MSG = "Category with id=%d was not found";
    public static final String USER_NOT_FOUND_MSG = "User with id=%s was not found";
    public static final String EVENT_NOT_FOUND_MSG = "Event with id=%s was not found";
    public static final String EVENT_WRONG_STATE_MSG = "Cannot %s the event because it's not in the right state: %s";
    public static final String EVENT_WRONG_UPDATE_DATE_MSG = "Cannot update the event because new date: %s is less than 1 hour before publish date.";

}
