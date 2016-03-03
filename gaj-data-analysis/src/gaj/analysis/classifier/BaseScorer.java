package gaj.analysis.classifier;

import gaj.data.classifier.Classifier;
import gaj.data.classifier.ClassifierScoreInfo;
import gaj.data.classifier.DataScorer;
import gaj.data.classifier.DatumScore;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.GoldDatum;
import gaj.data.classifier.ParameterisedClassifier;
import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;
import gaj.impl.vector.DataIterator;
import gaj.impl.vector.VectorFactory;
import java.util.Iterator;

/**
 * Binds an arbitrary scorer to gold-standard data.
 * <p/>
 * Assumes the overall accuracy score is the weighted average:
 * 
 * <pre>
 * <tt>s = sum_{d=1}^{N} w_d * s_d / sum_{d=1}^{N} w_d</tt>
 * </pre>
 * 
 * where <tt>s_d</tt> is the unweighted score of the <tt>d</tt>-th data-point, computed from knowing the posterior class probabilities P(c|x_d) for feature vector x_d, along with
 * the true class index c_d.
 * <p/>
 * Also, by default, no gradient or other information is computed. If this is required, override {@link #hasGradient}() and {@link #gradient}().
 */
public abstract class BaseScorer implements DataScorer {

    private final GoldData data;

    /**
     * Binds the given data to the scorer.
     * 
     * @param data - The gold-standard data set.
     */
    protected BaseScorer(GoldData data) {
        this.data = data;
    }

    @Override
    public int numClasses() {
        return data.numClasses();
    }

    @Override
    public int numFeatures() {
        return data.numFeatures();
    }

    /**
     * Computes the unweighted accuracy score of the given classification of feature vector x.
     * 
     * @param probs - The vector of posterior class probabilities, P(c|x).
     * @param classIndex - The index of the true class for x.
     * @return The unweighted score.
     */
    protected abstract double getScore(DataVector probs, int classIndex);

    @Override
    public boolean hasGradient() {
        return false;
    }

    /**
     * Computes the unweighted gradient of the classification score with respect to
     * the posterior classification probabilities.
     * 
     * @param probs - The vector of posterior class probabilities, P(c|x).
     * @param classIndex - The index of the true class for x.
     * @return The unweighted score gradient.
     * @throws IllegalStateException If no gradient can be computed.
     */
    protected DataVector getGradient(DataVector probs, int classIndex) {
        throw new IllegalStateException("Scorer is not gradient-enabled");
    }

    @Override
    public Iterable<? extends DatumScore> getDatumScores(final Classifier classifier) {
        return new Iterable<DatumScore>() {
            @Override
            public Iterator<DatumScore> iterator() {
                return new DataIterator<DatumScore>() {
                    private final Iterator<GoldDatum> iter = data.iterator();

                    @Override
                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    @Override
                    public DatumScore next() {
                        final GoldDatum datum = iter.next();
                        final DataVector probs = classifier.classify(datum.getFeatures()).posteriors();
                        return getDatumScore(datum, probs);
                    }
                };
            }
        };
    }

    @Override
    public double getClassifierScore(Classifier classifier) {
        double sumWeights = 0;
        double sumScores = 0;
        for (GoldDatum datum : data) {
            final double weight = datum.getWeight();
            sumWeights += weight;
            DataVector probs = classifier.classify(datum.getFeatures()).posteriors();
            final double unweightedScore = getScore(probs, datum.getClassIndex());
            sumScores += weight * unweightedScore;
        }
        return (sumWeights <= 0) ? Double.NEGATIVE_INFINITY : (sumScores / sumWeights);
    }

    @Override
    public ClassifierScoreInfo getClassifierScoreInfo(ParameterisedClassifier classifier) {
        if (!hasGradient() || !classifier.hasGradient()) {
            return getClassifierScore(getClassifierScore(classifier), null);
        } else {
            double sumWeights = 0;
            double sumScores = 0;
            WritableVector sumGradients = VectorFactory.newVector(classifier.numParameters());
            for (GoldDatum datum : data) {
                final double weight = datum.getWeight();
                sumWeights += weight;
                DataVector probs = classifier.classify(datum.getFeatures()).posteriors();
                DatumScore datumScore = getDatumScore(datum, probs);
                sumScores += weight * datumScore.getScore();
                DataVector gradient = VectorFactory.scale(classifier.getGradient(datumScore), weight);
                sumGradients.add(gradient);
            }
            if (sumWeights <= 0)
                return getClassifierScore(Double.NEGATIVE_INFINITY, null);
            double norm = 1.0 / sumWeights;
            return getClassifierScore(norm * sumScores, VectorFactory.scale(sumGradients, norm));
        }
    }

    private ClassifierScoreInfo getClassifierScore(final double score, final /*@Nullable*/ DataVector gradient) {
        return new ClassifierScoreInfo() {
            @Override
            public boolean hasGradient() {
                return (gradient != null);
            }

            @Override
            public double getScore() {
                return score;
            }

            @Override
            public DataVector getGradient() {
                if (gradient == null)
                    throw new IllegalStateException("No gradient was computed");
                return gradient;
            }
        };
    }

    private DatumScore getDatumScore(final GoldDatum datum, final DataVector probs) {
        final double score = getScore(probs, datum.getClassIndex());
        return new DatumScore() {
            private /*@MonotonicNonNull*/ DataVector gradient = null;

            @Override
            public double getScore() {
                return score;
            }

            @Override
            public double getWeight() {
                return datum.getWeight();
            }

            @Override
            public boolean hasGradient() {
                return BaseScorer.this.hasGradient();
            }

            @Override
            public DataVector getGradient() {
                if (gradient == null)
                    gradient = BaseScorer.this.getGradient(probs, datum.getClassIndex());
                return gradient;
            }

            @Override
            public DataVector getFeatures() {
                return datum.getFeatures();
            }

            @Override
            public int getClassIndex() {
                return datum.getClassIndex();
            }

            @Override
            public DataVector getPosteriors() {
                return probs;
            }
        };
    }
}
