package util;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeConversion 
{
    //Converts the UTC time used in the database to the local time on the user's machine.
    public static LocalDateTime utcToLocal(LocalDateTime utcDateTime)
    {
        ZoneId utcZone = ZoneId.of("UTC");
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime zonedUTC = ZonedDateTime.of(utcDateTime, utcZone);
        ZonedDateTime zonedLocal = zonedUTC.withZoneSameInstant(localZone);
        Month month = zonedLocal.getMonth();
        int day = zonedLocal.getDayOfMonth();
        int year = zonedLocal.getYear();
        int hour = zonedLocal.getHour();
        int minute = zonedLocal.getMinute();
        int second = zonedLocal.getSecond();
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute, second);
        return localDateTime;
    }
    
    //Converts the local time on the user's machine to UTC for storing time in the database.
    public static LocalDateTime localToUTC(LocalDateTime localDateTime)
    {
        ZoneId utcZone = ZoneId.of("UTC");
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime zonedLocal = ZonedDateTime.of(localDateTime, localZone);
        ZonedDateTime zonedUTC = zonedLocal.withZoneSameInstant(utcZone);
        Month month = zonedUTC.getMonth();
        int day = zonedUTC.getDayOfMonth();
        int year = zonedUTC.getYear();
        int hour = zonedUTC.getHour();
        int minute = zonedUTC.getMinute();
        int second = zonedUTC.getSecond();
        LocalDateTime utcDateTime = LocalDateTime.of(year, month, day, hour, minute, second);
        return utcDateTime;
    }
}
