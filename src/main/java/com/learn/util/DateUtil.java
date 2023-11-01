package com.learn.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DateUtil {

    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    public String createDate(LocalDateTime currentTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        return currentTime.format(formatter);

    }

}
