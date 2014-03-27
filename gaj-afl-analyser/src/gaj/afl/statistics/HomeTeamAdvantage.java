package gaj.afl.statistics;

import gaj.afl.data.DataFactory;
import gaj.afl.data.Fixture;
import gaj.afl.data.Match;
import gaj.afl.data.Team;

import java.io.IOException;
import java.util.Collection;


public class HomeTeamAdvantage {

	public static void main(String[] args) throws IOException {
		int N = 1 + Team.values().length;
		double[] numHomeWins = new double[N];
		double[] numHomeWinsSq = new double[N];
		int[] numHomeGames = new int[N];
		Collection<Match> records = DataFactory.newManager().getAllMatches();
		for (Match match : records) {
			Fixture fixture = match.getFixture();
			if (!fixture.getRound().toString().startsWith("R")) continue;
			double homeWin = scoreHomeWin(match);
			double homeWinSq = homeWin * homeWin;
			numHomeGames[0]++;
			numHomeWins[0] += homeWin;
			numHomeWinsSq[0] += homeWinSq;
			int n = 1 + fixture.getHomeTeam().ordinal();
			numHomeGames[n]++;
			numHomeWins[n] += homeWin; 
			numHomeWinsSq[n] += homeWinSq; 
		}
		
		summariseWins("Overall", numHomeGames[0], numHomeWins[0], numHomeWinsSq[0]);
		for (Team homeTeam : Team.values()) {
			int n = 1 + homeTeam.ordinal();
			summariseWins(homeTeam.toString(), numHomeGames[n], numHomeWins[n], numHomeWinsSq[n]);
		}
	}

	private static void summariseWins(String label, int numGames, double numWins, double numWinsSq) {
		double mean = numWins / numGames;
		double var = numWinsSq / numGames - mean * mean;
		double stdErr = Math.sqrt(var) / numGames;
		System.out.printf("%s -> %6.4f (%6.4f, %6.4f)%n", 
				label, mean, mean - 2*stdErr, mean + 2*stdErr);
	}

	private static double scoreHomeWin(Match record) {
		switch (record.getOutcome()) {
		case Draw:
			return 0.5;
		case Loss:
			return 0.0;
		case Win:
			return 1.0;
		default:
			throw new IllegalArgumentException("Unknown result: " + record.getOutcome());
		}
	}

}
