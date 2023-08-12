package com.bibliotech.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {

    public static LocalDateTime fromDate(String stringDate) throws DateTimeParseException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return  LocalDateTime.parse(stringDate, dateTimeFormatter);

    }





}
