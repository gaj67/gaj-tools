/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TestDate {

    private static final String[] TEST_DATES = new String[] {
        "Tuesday, 4 July 2013",
        "4/7/2013",
        "4-7-2013",
        "Thu Jul 04",
        "4/Jul/2013",
        "2013/Jul/04",
        "Thu Jul 04 00:00:00 CST 2013",
        "4 July 2013",
    };
    
    public static void main(String[] args) {
        for (String date : TEST_DATES) {
            testDate(date);
        }
    }

    private static void testDate(String date) {
        System.out.printf("Date: %s -> %s%n", date, parseDate(date));
    }

    private static final int[] INTERNAL_FORMATS = new int[] { DateFormat.SHORT, DateFormat.MEDIUM, DateFormat.LONG };
    private static final String[] DATE_FORMATS = new String[] { "dd/MMM/yyyy", "MMM/dd/yyyy", "yyyy/MMM/dd", "yyyy/MM/dd" };
    private static Date MIN_DATE;
    static {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, 1800);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        MIN_DATE = cal.getTime();
        System.out.println(MIN_DATE);
    }
    
    private static Date parseDate(String date) {
        if (date.contains("-")) date = date.replace('-', '/');
        if (date.contains(",")) {
            for (String subdate : date.split(",")) {
                Date _date = _parseDate(subdate);
                if (_date != null) return _date;
            }
            return null;
        }
        return _parseDate(date);
    }

    private static Date _parseDate(String date) {
        return date.contains(":") ? _parseDateTime(date) : _parseDateOnly(date);
    }

    private static Date _parseDateOnly(String date) {
        for (int dformat : INTERNAL_FORMATS) {
            try {
                return DateFormat.getDateInstance(dformat).parse(date);
            } catch (ParseException e) {
            }
        }
        for (String dformat : DATE_FORMATS) {
            try {
                Date _date = new SimpleDateFormat(dformat).parse(date);
                if (_date != null && _date.after(MIN_DATE)) return _date;
            } catch (ParseException e) {
            }
        }
        return null;
    }

    private static Date _parseDateTime(String date) {
        for (int dformat : INTERNAL_FORMATS) {
            for (int tformat : INTERNAL_FORMATS) {
                try {
                    return DateFormat.getDateTimeInstance(dformat, tformat).parse(date);
                } catch (ParseException e) {
                }
            }
        }
        return null;
    }

}
