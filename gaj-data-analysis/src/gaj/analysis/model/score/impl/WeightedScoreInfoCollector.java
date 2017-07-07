package gaj.analysis.model.score.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import gaj.analysis.model.score.ScoreInfo;
import gaj.analysis.model.score.WeightedScoreInfo;
import gaj.common.annotations.PackagePrivate;

@PackagePrivate class WeightedScoreInfoCollector implements Collector<WeightedScoreInfo, WeightedScoreInfoAccumulator, ScoreInfo>
{

    private static final Set<Characteristics> CHARACTERISTICS = new HashSet<Characteristics>();
    static {
        CHARACTERISTICS.add(Characteristics.UNORDERED);
    }

    @PackagePrivate WeightedScoreInfoCollector() {}

    @Override
    public Supplier<WeightedScoreInfoAccumulator> supplier() {
        return WeightedScoreInfoAccumulator::new;
    }

    @Override
    public BiConsumer<WeightedScoreInfoAccumulator, WeightedScoreInfo> accumulator() {
        return WeightedScoreInfoAccumulator::accumulate;
    }

    @Override
    public BinaryOperator<WeightedScoreInfoAccumulator> combiner() {
        return WeightedScoreInfoAccumulator::combine;
    }

    @Override
    public Function<WeightedScoreInfoAccumulator, ScoreInfo> finisher() {
        return WeightedScoreInfoAccumulator::getScoreInfo;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return CHARACTERISTICS;
    }

}
