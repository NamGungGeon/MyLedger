package site.cpsp.myledger.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.Calendar;

/**
 * Created by WINDOWS7 on 2019-04-29.
 */

public class TimeUtils {
    private TimeUtils(){}


    public static final String formatString= "yyyy-MM-dd HH:mm";

    public static String getCurrentDateAndTime(){
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat format= new SimpleDateFormat(formatString);
        return format.format(calendar.getTime());
    }
    private static String extractor(String str){
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat format= new SimpleDateFormat(str);
        return format.format(calendar.getTime());
    }
    public static String getCurrentYear(){
        return extractor("yyyy");
    }
    public static String getCurrentMonth(){
        return String.valueOf(Integer.valueOf(extractor("MM"))-1);
    }
    public static String getCurrentDay(){
        return extractor("dd");
    }
    public static String getCurrentHour(){
        return extractor("HH");
    }
    public static String getCurrentMinute(){
        return extractor("mm");
    }

    public static int getDiff(String t1, String t2){
        SimpleDateFormat format= new SimpleDateFormat(formatString);
        try {
            java.util.Date d1= format.parse(t1);
            java.util.Date d2= format.parse(t2);
            return d1.compareTo(d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
