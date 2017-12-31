package gaj.analysis.model.score.impl;

import gaj.analysis.model.score.ScoreInfo;
import gaj.analysis.model.score.WeightedScoreInfo;
import gaj.common.annotations.PackagePrivate;

@PackagePrivate class WeightedScoreInfoAccumulator {

    private double sumWeight = 0.0;
    private double sumScore = 0.0;

    @PackagePrivate void accumulate(WeightedScoreInfo scoreInfo) {
        sumWeight += scoreInfo.getWeight();
        sumScore += scoreInfo.getWeight() * scoreInfo.getScore();
        // TODO add in score info gradient if weight != 0
    }

    @PackagePrivate WeightedScoreInfoAccumulator combine(WeightedScoreInfoAccumulator accumulator) {
        // TODO add in accumulator score info gradient
        sumWeight += accumulator.sumWeight;
        sumScore += accumulator.sumScore;
        return this;
    }

    @PackagePrivate ScoreInfo getScoreInfo() {
        // TODO encapsulate score info gradient
        final double _score = (sumWeight == 0.0) ? 0.0 : sumScore / sumWeight;
        return new ScoreInfo() {
            @Override
            public double getScore() {
                return _score;
            }
        };
    }

}
