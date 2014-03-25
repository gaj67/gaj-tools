package gaj.afl.datatype;

import gaj.afl.data.ShortMonth;

/**
 * The date and time of a match.
 */
public class MatchDateTime {

   public enum Day { Sun, Mon, Tue, Wed, Thu, Fri, Sat };

   public final int year, day;
   public final ShortMonth month;
   public final Day dayOfWeek;
   public final String time; // For now; format: "<hr>:<mins><am|pm>".

   /**
    * @param year - the string value of the year.
    * @param date - a string in the format: "&lt;month&gt; &lt;day&gt; (&lt;dayOfWeek&gt; &lt;time&gt;)".
    */
   public MatchDateTime(String year, String date) {
      this.year = Integer.valueOf(year);
      String[] parts = date.split(" ");
      this.month = ShortMonth.valueOf(parts[0]);
      this.day = Integer.valueOf(parts[1]);
      this.dayOfWeek = Day.valueOf(parts[2].substring(1));
      this.time = parts[3].substring(0, parts[3].length()-1);
   }

   /**
    * @param date - a string in the format: "&lt;year&gt; &lt;month&gt; &lt;day&gt; (&lt;dayOfWeek&gt; &lt;time&gt;)".
    */
   public MatchDateTime(String date) {
      String[] parts = date.split(" ");
      this.year = Integer.valueOf(parts[0]);
      this.month = ShortMonth.valueOf(parts[1]);
      this.day = Integer.valueOf(parts[2]);
      this.dayOfWeek = Day.valueOf(parts[3].substring(1));
      this.time = parts[4].substring(0, parts[4].length()-1);
   }

   public String toString() {
	   return String.format("%d %s %d (%s %s)", year, month, day, dayOfWeek, time);
   }
}
