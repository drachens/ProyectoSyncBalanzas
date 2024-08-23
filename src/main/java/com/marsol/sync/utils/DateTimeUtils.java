package com.marsol.sync.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static LocalDateTime stringToDateTime(String value){
        try{
            return LocalDateTime.parse(value, FORMATTER);
        }catch(DateTimeParseException e){
            System.out.println("Error parsing date, skipping: " + value);
            return null;
        }

    }

    public static String dateTimeToString(LocalDateTime value){
        return FORMATTER.format(value);
    }
}
