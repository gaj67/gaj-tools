package afl.classifier;

public class Stats {

    public int numData = 0;
    public double meanScore = 0;
    public double meanError = 0;
    public double varScore = 0;
    public double varError = 0;

    public String toString() {
        return String.format(
                "num[data]=%d, mean[score]=%9.6f, var[score]=%9.6f, mean[error]=%5.3f, var[error]=%5.3f",
                numData, meanScore, varScore, meanError, varError
                );
    }

    public static Stats macroAverage(Stats... multiStats) {
        Stats average = new Stats();
        double sumMeanScore = 0, sumMeanError = 0;
        double sumSqMeanScore = 0, sumSqMeanError = 0;
        final int n = multiStats.length;
        average.numData = n;
        for (Stats stats : multiStats) {
            sumMeanScore += stats.meanScore;
            sumMeanError += stats.meanError;
            sumSqMeanScore += stats.meanScore * stats.meanScore;
            sumSqMeanError += stats.meanError * stats.meanError;
        }
        average.meanScore = sumMeanScore / n;
        average.meanError = sumMeanError / n;
        average.varScore = sumSqMeanScore / n - average.meanScore * average.meanScore;
        average.varError = sumSqMeanError / n - average.meanError * average.meanError;
        return average;
    }

    private static Stats _macroAverage(Stats... multiStats) {
        Stats average = new Stats();
        double sumMeanScore = 0, sumMeanError = 0;
        double sumVarScore = 0, sumVarError = 0;
        final int n = multiStats.length;
        average.numData = n;
        for (Stats stats : multiStats) {
            sumMeanScore += stats.meanScore;
            sumMeanError += stats.meanError;
            sumVarScore += stats.varScore;
            sumVarError += stats.varError;
        }
        average.meanScore = sumMeanScore / n;
        average.meanError = sumMeanError / n;
        average.varScore = sumVarScore / n;
        average.varError = sumVarError / n;
        return average;
    }

    public static Stats microAverage(Stats... multiStats) {
        Stats average = new Stats();
        double sumScore = 0, sumError = 0;
        double sumSqScore = 0, sumSqError = 0;
        int sumNumData = 0;
        for (Stats stats : multiStats) {
            sumNumData += stats.numData;
            sumScore += stats.numData * stats.meanScore;
            sumError += stats.numData * stats.meanError;
            sumSqScore += stats.numData * (stats.varScore + stats.meanScore * stats.meanScore);
            sumSqError += stats.numData * (stats.varError + stats.meanError * stats.meanError);
        }
        average.numData = sumNumData;
        average.meanScore = sumScore / sumNumData;
        average.meanError = sumError / sumNumData;
        average.varScore = sumSqScore / sumNumData - average.meanScore * average.meanScore;
        average.varError = sumSqError / sumNumData - average.meanError * average.meanError;
        return average;
    }

}
