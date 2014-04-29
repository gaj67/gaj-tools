/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class TestDateFromat {

    private static final int[] FORMATS = { DateFormat.SHORT, DateFormat.MEDIUM, DateFormat.LONG };

    /**
     * @param args
     */
    public static void main(String[] args) {
        // Make a new Date object. It will be initialized to the current time.
        Date now = new Date();

        // See what toString() returns
        testDate(now.toString());

        // Next, try the default DateFormat
        testDate(DateFormat.getInstance().format(now));

        // And the default time and date-time DateFormats
        testDate(DateFormat.getDateInstance().format(now));
        testDate(DateFormat.getDateTimeInstance().format(now));

        // Next, try the short, medium and long variants of the 
        // default date format
        for (int format : FORMATS)
            testDate(DateFormat.getDateInstance(format).format(now));

        // For the default date-time format, the length of both the
        // date and time elements can be specified.
        for (int dformat : FORMATS)
            for (int tformat : FORMATS)
                testDate(DateFormat.getDateTimeInstance(dformat, tformat).format(now));
    }

    private static void testDate(String date) {
        System.out.printf("* now -> %s -> %s%n", date, parseDate(date));
    }
    
    private static Date parseDate(String date) {
        for (int dformat : FORMATS) {
            for (int tformat : FORMATS) {
                try {
                    return DateFormat.getDateTimeInstance(dformat, tformat).parse(date);
                } catch (ParseException e) {
                }
            }
        }
        for (int dformat : FORMATS) {
            try {
                return DateFormat.getDateInstance(dformat).parse(date);
            } catch (ParseException e) {
            }
        }
        return null;
    }
}
