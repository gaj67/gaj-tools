package gaj.analysis.entropy;

import java.util.HashMap;
import java.util.Map;
import gaj.analysis.data.numeric.vector.IndexVector;

public abstract class EntropyAnalyser {

    private EntropyAnalyser() {
    }

    /**
     * Computes the empirical entropy of a discrete sequence
     * of observations.
     * 
     * @param sequence - The observation sequence.
     * @return The entropy measure.
     */
    public static double entropy(int[] sequence) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (int obs : sequence) {
            Integer count = counts.get(obs);
            count = (count == null) ? 1 : (count + 1);
            counts.put(obs, count);
        }
        final double norm = 1.0 / sequence.length;
        double H = 0;
        for (int count : counts.values()) {
            double p = norm * count;
            H -= p * Math.log(p);
        }
        return H;
    }

    /**
     * Computes the empirical entropy of a discrete sequence
     * of observations.
     * 
     * @param sequence - The observation sequence.
     * @return The entropy measure.
     */
    public static double entropy(IndexVector sequence) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (Integer obs : sequence) {
            Integer count = counts.get(obs);
            count = (count == null) ? 1 : (count + 1);
            counts.put(obs, count);
        }
        final double norm = 1.0 / sequence.size();
        double H = 0;
        for (int count : counts.values()) {
            double p = norm * count;
            H -= p * Math.log(p);
        }
        return H;
    }

    /**
     * Computes the empirical entropy of a discrete sequence
     * of observations.
     * 
     * @param sequence - The observation sequence.
     * @return The entropy measure.
     */
    public static <T> double entropy(T[] sequence) {
        Map<T, Integer> counts = new HashMap<>();
        for (T obs : sequence) {
            Integer count = counts.get(obs);
            count = (count == null) ? 1 : (count + 1);
            counts.put(obs, count);
        }
        final double norm = 1.0 / sequence.length;
        double H = 0;
        for (int count : counts.values()) {
            double p = norm * count;
            H -= p * Math.log(p);
        }
        return H;
    }

    /**
     * Computes the empirical joint entropy of a pair of discrete sequences
     * of observations.
     * 
     * @param sequence1 - The first observation sequence.
     * @param sequence2 - The second observation sequence.
     * @return The entropy measure.
     */
    public static double entropy(int[] sequence1, int[] sequence2) {
        Map<IntPair, Integer> counts = new HashMap<>();
        final int length = sequence1.length;
        for (int i = 0; i < length; i++) {
            IntPair obs = new IntPair(sequence1[i], sequence2[i]);
            Integer count = counts.get(obs);
            count = (count == null) ? 1 : (count + 1);
            counts.put(obs, count);
        }
        final double norm = 1.0 / length;
        double H = 0;
        for (int count : counts.values()) {
            double p = norm * count;
            H -= p * Math.log(p);
        }
        return H;
    }

}
