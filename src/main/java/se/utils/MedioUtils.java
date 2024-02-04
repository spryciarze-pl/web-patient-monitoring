package se.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MedioUtils {

    private static final DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss", Locale.ENGLISH);
    private static final DateTimeFormatter formatterDefault = DateTimeFormatter.ofPattern("dd MM yyyy HH:mm:ss", Locale.ENGLISH);

    public static String formatDateTimeWithFullMonthName(LocalDateTime dateTime) {
        return dateTime.format(formatterMonth);
    }

    public static String formatDateTimeDefault(LocalDateTime dateTime) {
        return dateTime.format(formatterDefault);
    }

}
