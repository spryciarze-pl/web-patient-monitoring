package se.utils;

import se.db.model.Appointment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MedioUtils {

    private static final DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss", Locale.ENGLISH);
    private static final DateTimeFormatter formatterDefault = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH);

    public static String formatDateTimeWithFullMonthName(LocalDateTime dateTime) {
        return dateTime.format(formatterMonth);
    }

    public static String formatDateTimeDefault(LocalDateTime dateTime) {
        return dateTime.format(formatterDefault);
    }

    public static String getReaminingDuration(LocalDateTime dateTime) {
        Duration duration = Duration.between(LocalDateTime.now(), dateTime);
        return (duration.toDays() != 0 ? duration.toDays() + " days " : "") + duration.toHours() % 24 + " hours";
    }

    public static String toStringDuration(Appointment appointment) {
        Duration duration = Duration.between(appointment.getAppointmentTime(), appointment.getAppointmentEnd());
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return String.format("%02d:%02d", hours, minutes);
    }

}
