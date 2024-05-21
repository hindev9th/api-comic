package com.example.test.demo.Helpers;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeConvertHelper {
    public static Date convertTimeAgoToDateTime(String timeAgo) {
        ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDateTime currentDateTime = LocalDateTime.now(vietnamZone);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/uu");
        LocalDateTime resultDateTime = null;
        String[] parts = timeAgo.split(" ");

        try{
            int amount = Integer.parseInt(parts[0]);
            String unit = parts[1].toLowerCase();

            if (unit.contains("giây")) resultDateTime = currentDateTime.minusSeconds(amount);
            if (unit.contains("phút")) resultDateTime = currentDateTime.minusMinutes(amount);
            if (unit.contains("giờ")) resultDateTime = currentDateTime.minusHours(amount);
            if (unit.contains("ngày")) resultDateTime = currentDateTime.minusDays(amount);
            if (unit.contains("tháng")) resultDateTime = currentDateTime.minusMonths(amount);
            if (unit.contains("năm")) resultDateTime = currentDateTime.minusYears(amount);

        }catch(Exception e){
            try {
                if (parts[1].split("/").length < 3){
                    timeAgo += "/24";
                }
            }catch (Exception ex){
                timeAgo = "00:00 " + timeAgo;
            }

            resultDateTime = LocalDateTime.parse(timeAgo, formatter);
        }

        long epochSeconds = resultDateTime.atZone(vietnamZone).toEpochSecond();
        return new Date(epochSeconds * 1000);
    }
}
