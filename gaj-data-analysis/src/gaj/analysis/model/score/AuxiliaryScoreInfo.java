package gaj.analysis.model.score;

import gaj.analysis.model.AuxiliaryInfo;

/**
 * Indicates auxiliary information that is targeted specifically to a model
 * scorer.
 */
public interface AuxiliaryScoreInfo extends AuxiliaryInfo {

    /**
     * Indicates that unlabelled data are scored, if possible, rather than simply ignored.
     */
    static interface AllowUnlabelled { }

    /**
     * Indicates that gradient information should be computed, if possible.
     */
    final static AllowUnlabelled ALLOW_UNLABELLED = new AllowUnlabelled() {};

    /**
     * Indicates whether or not unlabelled data should be scored, if possible,
     * rather than simply ignored.
     * 
     * @param info
     *            - Optional objects either specifying auxiliary information for
     *            the model, or requesting auxiliary information be provided for
     *            or with the score.
     * @return A value of true (or false) if unlabelled cases are (or are not)
     *         to be scored.
     */
    static boolean isUnlabelledAllowed(AuxiliaryInfo... info) {
        for (AuxiliaryInfo infoObj : info) {
            if (infoObj instanceof AllowUnlabelled) return true;
        }
        return false;
    }

}
