package gaj.afl.statistics;
import gaj.afl.data.MatchDataFactory;
import gaj.afl.data.match.Match;
import gaj.afl.data.match.MatchFetcher;
import gaj.afl.data.match.Outcome;
import gaj.afl.data.match.Team;
import gaj.analysis.vector.DataIterator;
import gaj.analysis.vector.VectorFactory;
import gaj.analysis.vector.VectorIterative;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.GoldDatum;
import gaj.data.vector.DataVector;

import java.util.Collection;
import java.util.Iterator;



public class TrainHomeTeamAdvantage {

	public static void main(String[] args) {
		// Collect all match statistics...
		MatchFetcher manager = MatchDataFactory.newManager();
		GoldData trainingData = getMatchData(manager.getMatchesByYear(2010, 2011));
		GoldData testingData = getMatchData(manager.getMatchesByYear(2012));
	}

	private static GoldData getMatchData(final Collection<Match> matches) {
		return new GoldData() {
			private final DataVector FEATURE_VEC = VectorFactory.newVector(1, 0, 1.);

			@Override
			public int numClasses() {
				return 2;
			}

			@Override
			public int numFeatures() {
				return 1;
			}

			@Override
			public Iterator<GoldDatum> iterator() {
				return new VectorIterative<GoldDatum>(matches.size()) {
					private final Iterator<Match> iter = matches.iterator();
					private Match match = null;

					@Override
					public boolean hasNext() {
						// Skip draws for now.
						while (iter.hasNext()) {
							match = iter.next();
							if (Outcome.Draw != match.getOutcome())
								return true;
						}
						return false;
					}

					@Override
					protected GoldDatum get(int pos) {
						return new GoldDatum() {
							@Override
							public DataVector getFeatures() {
								return FEATURE_VEC;
							}

							@Override
							public int getClassIndex() {
								return (Outcome.Loss == match.getOutcome()) ? 0 : 1;
							}

							@Override
							public double getWeight() {
								return 1.0;
							}							
						};
					}
				};
			}
		};
	}

}
