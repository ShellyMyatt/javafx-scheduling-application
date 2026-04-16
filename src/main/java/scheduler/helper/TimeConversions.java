package scheduler.helper;


import java.time.*;
import java.time.format.DateTimeFormatter;


public class TimeConversions {

    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    private static final ZoneId EASTERN_ZONE = ZoneId.of("America/New_York");
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DEFAULT_FORMATTER);
    }

    public static String formatZonedDateTime(ZonedDateTime dateTime) {
        return dateTime.format(DEFAULT_FORMATTER);
    }
    /**
     * This method converts local date time in the systems default time zone and converts it to UTC.
     */
    public static LocalDateTime toUTC(LocalDate localDate, LocalTime localTime) {
        // Combines the users LocalDate and LocalTime in the user's local time zone into a ZonedDateTime
        ZonedDateTime userDateTime = ZonedDateTime.of(localDate, localTime, ZoneId.systemDefault());

        //Converts the user's ZonedDateTime we just got above, and converts it from the user's local time zone to UTC
        ZonedDateTime utcDateTime = userDateTime.withZoneSameInstant(ZoneId.of("UTC"));

        // returns the time (in UTC) without the time zone information.
        // The database expects a time-zone neutral LocalDateTime, this line strips the time zone (UTC) from the ZonedDateTime
        return utcDateTime.toLocalDateTime();
    }

    /**
     * Converts a LocalDateTime in UTC into a LocalDaeTime in the users local time zone, the system's default local time zone.
     * The Application needs to show the date and time in the user's local time zone, not in UTC.
     *
     * @param utcDateTime The date and time in UTC.
     * @return The corresponding LocalDateTime in the user's local time zone.
     */
    public static LocalDateTime toLocalTime(LocalDateTime utcDateTime) {
        // Attaches the UTC time zone to the input LocalDateTime
        // Converts the LocalDateTime to a ZonedDateTime by associating it with the UTC time zone.
        ZonedDateTime utcZone = utcDateTime.atZone(ZoneId.of("UTC"));

        // Converts the ZonedDateTime from UTC time zone to the user's local time zone
        ZonedDateTime localDateTime = utcZone.withZoneSameInstant(ZoneId.systemDefault());

        // Strips the time zone information from the ZonedDateTime, leaving just the local date and time as a LocalDateTime for display.
        return localDateTime.toLocalDateTime();
    }

    /**
     * Checks if a given Local date and time are within the business hours of 8:00am to 10:00pm Eastern Time.
     *
     * @param localDate the date in the user's local time zone.
     * @param localTime the time in the user's local time zone.
     * @return true if the given date and time are within business hours, false if not.
     */
    public static boolean isWithinBusinessHours(LocalDate localDate, LocalTime localTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate, localTime, ZoneId.systemDefault());
        ZonedDateTime easternTime = zonedDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(22, 0);
        return !easternTime.toLocalTime().isBefore(start) && !easternTime.toLocalTime().isAfter(end);
    }



}
