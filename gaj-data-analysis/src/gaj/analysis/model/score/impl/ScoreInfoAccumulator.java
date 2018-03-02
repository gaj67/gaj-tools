package gaj.analysis.model.score.impl;

import gaj.analysis.data.numeric.vector.DataVector;
import gaj.analysis.data.numeric.vector.impl.VectorFactory;
import gaj.analysis.model.score.GradientScoreInfo;
import gaj.analysis.model.score.ScoreInfo;
import gaj.common.annotations.PackagePrivate;

@PackagePrivate class ScoreInfoAccumulator {

    private double sumWeight = 0.0;
    private double sumScore = 0.0;
    private DataVector sumGradient = null;

    @PackagePrivate void accumulate(ScoreInfo scoreInfo) {
        final double weight = scoreInfo.getWeight();
        if (weight != 0) {
            sumWeight += weight;
            sumScore += weight * scoreInfo.getScore();
            if (scoreInfo instanceof GradientScoreInfo) {
                DataVector _gradient = ((GradientScoreInfo) scoreInfo).getScoreGradient();
                accumulateGradient(VectorFactory.multiply(_gradient, weight));
            }
        }
    }

    private void accumulateGradient(DataVector gradient) {
        if (sumGradient == null) {
            sumGradient = gradient;
        } else {
            sumGradient = VectorFactory.add(sumGradient, gradient);
        }
    }

    @PackagePrivate ScoreInfoAccumulator combine(ScoreInfoAccumulator accumulator) {
        // TODO add in accumulator score info gradient
        sumWeight += accumulator.sumWeight;
        sumScore += accumulator.sumScore;
        accumulateGradient(accumulator.sumGradient);
        return this;
    }

    @PackagePrivate ScoreInfo getScoreInfo() {
        final double scaling = (sumWeight == 0.0) ? 0.0 : 1 / sumWeight;
        final double _score = scaling * sumScore;
        if (sumGradient == null) {
            return new ScoreInfo() {
                @Override
                public double getScore() {
                    return _score;
                }

                @Override
                public double getWeight() {
                    return 1.0;
                }
            };
        } else {
            final DataVector _gradient = VectorFactory.multiply(sumGradient, scaling);
            return new GradientScoreInfo() {
                @Override
                public double getScore() {
                    return _score;
                }

                @Override
                public double getWeight() {
                    return 1.0;
                }

                @Override
                public DataVector getScoreGradient() {
                    return _gradient;
                }
            };
        }
    }

}
