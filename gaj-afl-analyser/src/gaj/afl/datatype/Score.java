package gaj.afl.datatype;

/**
 * Specifies a score consisting of goals and behinds.
 * This could be a quarter-time score or a total score.
 */
public class Score {

   public final int goals;
   public final int behinds;
   public final int points;

   public Score(int goals, int behinds) {
      this.goals = goals;
      this.behinds = behinds;
      this.points = behinds + 6 * goals;
   }

   /**
    * @param strings - a pair of strings specifying the number of goals
    * and the number of behinds scored.
    */
   public Score(String...strings) {
      this(Integer.valueOf(strings[0]), Integer.valueOf(strings[1]));
   }

   /**
    * @param score - a string in the format: "&lt;goals&gt;.&lt;behinds&gt;".
    */
   public Score(String score) {
      this(score.split("[.]"));
   }

   public String toString() {
	   return String.format("%d.%d (%d)", goals, behinds, points);
   }

   public Score subtract(Score score) {
      if (score == null) return this;
      return new Score(this.goals - score.goals, this.behinds - score.behinds);
   }

}
