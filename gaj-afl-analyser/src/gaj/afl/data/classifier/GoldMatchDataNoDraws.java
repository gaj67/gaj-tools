package gaj.afl.data.classifier;

import java.util.Collection;
import java.util.Iterator;

import gaj.afl.data.match.Match;
import gaj.afl.data.match.Outcome;
import gaj.analysis.vector.VectorIterative;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.GoldDatum;
import gaj.data.vector.DataVector;

/**
 * Interprets a collection of historical match data as a 
 * collection of gold-standard feature data.
 * <p/>Note: A loss to the home team is specified by a 
 * class index of 0, and a win is specified by 
 * a class index of 1. 
 * Drawn matches are ignored.
 *
 */
/*package-private*/ abstract class GoldMatchDataNoDraws implements GoldData {

	private final Collection<Match> matches;

	/*package-private*/ GoldMatchDataNoDraws(Collection<Match> matches) {
		this.matches = matches;
	}
	
	@Override
	public int numClasses() {
		return 2;
	}

	@Override
	public abstract int numFeatures();

	protected abstract DataVector getFeatures(Match match);

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
						return GoldMatchDataNoDraws.this.getFeatures(match);
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

}
