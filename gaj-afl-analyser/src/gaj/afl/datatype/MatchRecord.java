package gaj.afl.datatype;




public class MatchRecord {

   public String round;
   public MatchDateTime date;
   public MatchLocation location;
   public TeamScores homeTeam, awayTeam;
   public MatchResult homeTeamResult;

   public MatchRecord(String round, MatchDateTime date, MatchLocation location, TeamScores homeTeam, TeamScores awayTeam, MatchResult homeTeamResult) {
	  this.round = round;
      this.date = date;
      this.location = location;
      this.homeTeam = homeTeam;
      this.awayTeam = awayTeam;
      this.homeTeamResult = homeTeamResult;
   }

   public String toString() {
	   return String.format("%s %s %s %s %s %s", round, homeTeam, homeTeamResult, awayTeam, location, date);
   }
}
