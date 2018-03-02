package gaj.analysis.model.score.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import gaj.analysis.model.score.ScoreInfo;
import gaj.common.annotations.PackagePrivate;

@PackagePrivate class ScoreInfoCollector implements Collector<ScoreInfo, ScoreInfoAccumulator, ScoreInfo>
{

    @SuppressWarnings("serial")
    private static final Set<Characteristics> CHARACTERISTICS = new HashSet<Characteristics>() {{
        add(Characteristics.UNORDERED);
    }};

    @PackagePrivate ScoreInfoCollector() {}

    @Override
    public Supplier<ScoreInfoAccumulator> supplier() {
        return ScoreInfoAccumulator::new;
    }

    @Override
    public BiConsumer<ScoreInfoAccumulator, ScoreInfo> accumulator() {
        return ScoreInfoAccumulator::accumulate;
    }

    @Override
    public BinaryOperator<ScoreInfoAccumulator> combiner() {
        return ScoreInfoAccumulator::combine;
    }

    @Override
    public Function<ScoreInfoAccumulator, ScoreInfo> finisher() {
        return ScoreInfoAccumulator::getScoreInfo;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return CHARACTERISTICS;
    }

}
