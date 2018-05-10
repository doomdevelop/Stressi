package org.wirbleibenalle.stressi.util;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public class DateUtil {
    public static String formatDateForTitle(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EE d MMMM y").withLocale(Locale.GERMANY);
        return localDate.toString(formatter);
    }
}
