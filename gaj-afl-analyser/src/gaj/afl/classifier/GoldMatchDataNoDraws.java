package gaj.afl.classifier;

import gaj.afl.data.core.Match;
import gaj.afl.data.core.Outcome;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.GoldDatum;
import gaj.data.vector.DataVector;
import gaj.impl.vector.VectorIterative;
import java.util.Collection;
import java.util.Iterator;

/**
 * Interprets a collection of historical match data as a
 * collection of gold-standard feature data.
 * <p/>Note: A loss to the home team is specified by a
 * class index of 0, and a win is specified by
 * a class index of 1.
 * Drawn matches are ignored.
 *
 */
public abstract class GoldMatchDataNoDraws implements GoldData {

    private final Collection<Match> matches;

    public GoldMatchDataNoDraws(Collection<Match> matches) {
	this.matches = matches;
    }

    @Override
    public int numClasses() {
	return 2;
    }

    @Override
    public abstract int numFeatures();

    /**
     * Obtains the numerical features for the match.
     *
     * @param match - The match.
     * @return The match feature vector.
     */
    protected abstract DataVector getFeatures(Match match);

    @Override
    public Iterator<GoldDatum> iterator() {
	return new VectorIterative<GoldDatum>(matches.size()) {
	    private final Iterator<Match> iter = matches.iterator();
	    private Match match = null;

	    @Override
	    public boolean hasNext() {
		while (match == null) {
		    if (!iter.hasNext()) {
			return false;
		    }
		    match = iter.next();
		    if (Outcome.Draw == match.getOutcome()) {
			match = null;
		    }
		}
		return true;
	    }

	    @Override
	    protected GoldDatum get(int pos) {
		final Match _match = match;
		match = null;
		return new GoldDatum() {
		    @Override
		    public DataVector getFeatures() {
			return GoldMatchDataNoDraws.this.getFeatures(_match);
		    }

		    @Override
		    public int getClassIndex() {
			return (Outcome.Loss == _match.getOutcome()) ? 0 : 1;
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
