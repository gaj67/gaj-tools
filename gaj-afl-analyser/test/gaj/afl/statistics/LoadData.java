package gaj.afl.statistics;
import gaj.afl.data.MatchDataFactory;
import gaj.afl.data.classifier.GoldMatchDataNoDraws;
import gaj.afl.data.match.Match;
import gaj.afl.data.match.MatchFetcher;
import gaj.afl.data.match.Outcome;
import gaj.analysis.vector.VectorFactory;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.GoldDatum;
import gaj.data.vector.DataVector;

import java.util.Collection;


public class LoadData {

	private static final DataVector FEATURE_VECTOR = VectorFactory.newSparseVector(1);

	public static void main(String[] args) {
		for (String arg : args)
			examineData(Integer.parseInt(arg));
	}

	private static void examineData(int year) {
		System.out.printf("Examining data for year %d...%n", year);
		MatchFetcher manager = MatchDataFactory.newManager();
		Collection<Match> matches = manager.getMatchesByYear(year);
		{
			int n = 0, w = 0, l = 0;
			for (Match match : matches) {
				n++;
				if (Outcome.Win == match.getOutcome()) w++;
				else if (Outcome.Loss == match.getOutcome()) l++;
			}
			double p = 1.0 * w / n;
			double q = 1.0 * l / n;
			System.out.printf("#games=%d, home-losses=%d, home-wins=%d, P(home-win)=%5.3f, P(home-loss)=%5.3f%n", n, l, w, p, q);


		}
		{
			GoldData trainingData = getMatchData(matches);
			int n = 0, w = 0;
			for (GoldDatum datum : trainingData) {
				n++;
				if (datum.getClassIndex() == 1) w++;
			}
			double p = 1.0 * w / n;
			System.out.printf("#games=%d, home-losses=%d, home-wins=%d, P(home-win)=%5.3f, P(home-loss)=%5.3f%n", n, n-w, w, p, 1-p);
		}
	}

	private static GoldData getMatchData(final Collection<Match> matches) {
		return new GoldMatchDataNoDraws(matches) {
			@Override
			public int numFeatures() {
				return 1;
			}

			@Override
			protected DataVector getFeatures(Match match) {
				return FEATURE_VECTOR;
			}
		};
	}
}
