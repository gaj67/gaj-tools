package gaj.afl.datatype;

import gaj.afl.data.Team;


public class TeamScores {

   public Team team;
   public Score[] scores;

   public TeamScores(Team team, Score... scores) {
      this.team = team;
      this.scores = scores;
   }

   public String toString() {
	   StringBuilder buffer = new StringBuilder();
	   buffer.append(team.toString());
	   buffer.append(':');
	   for (Score score : scores) {
		   buffer.append(' ');
		   buffer.append(score.toString());
	   }
	   return buffer.toString();
   }
}
