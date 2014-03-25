package afl.analyser;

import gaj.afl.datatype.MatchResult;

/**
 * Stores the home and away win/loss statistics.
 */
public class WinLossStats {

	private static final double EPSILON = 1e-16;

	private static enum Place {
		home, away;
		
		private static Place fromBoolean(boolean isHome) {
			return isHome ? home : away;
		}
	}
	
	private double[][] counts = new double[Place.values().length][MatchResult.values().length];

	private void add(Place place, MatchResult result, double count) {
		counts[place.ordinal()][result.ordinal()] += count;
	}

	/**
	 * Accumulates the statistics from a single match.
	 * 
	 * @param result - The match result.
	 * @param isHomeTeam - A flag indicating whether to update the home-team
	 * statistics (true) or the away-team statistics (false).
	 */
	public void add(MatchResult result, boolean isHomeTeam) {
		add(Place.fromBoolean(isHomeTeam), result, 1);
	}

	public double numHomeGames() {
		double total = 0;
		for (double count : counts[Place.home.ordinal()]) 
			total += count;
		return total;
	}

	public double numAwayGames() {
		double total = 0;
		for (double count : counts[Place.away.ordinal()]) 
			total += count;
		return total;
	}

	public double numHomeWins() {
		
	}
	
	public String toString() {
		double homeTotal = numHomeGames();
		double homeRatio = (homeTotal < EPSILON) ? 0.5 : counts[Index.homeWins.ordinal()] / homeTotal;
		double awayTotal = counts[Index.awayWins.ordinal()] + counts[Index.awayLosses.ordinal()];
		double awayRatio = (awayTotal < EPSILON) ? 0.5 : counts[Index.awayWins.ordinal()] / awayTotal;
		return String.format(
				"hw=%3.1f, hl=%3.1f [hr=%4.2f], aw=%3.1f, al=%3.1f [ar=%4.2f]",
				counts[Index.homeWins.ordinal()],
				counts[Index.homeLosses.ordinal()],
				homeRatio,
				counts[Index.awayWins.ordinal()],
				counts[Index.awayLosses.ordinal()],
				awayRatio
		);
	}
}