package gaj.analysis.model.score;

import gaj.analysis.model.AuxiliaryInfo;

/**
 * Indicates auxiliary information that is targeted specifically to a model
 * scorer.
 */
public interface ScorerInfo extends AuxiliaryInfo {

    /**
     * Indicates that unlabelled data are scored, if possible, rather than simply ignored.
     */
    static interface UnlabelledScorerInfo extends ScorerInfo {}

    /**
     * Indicates that score information should be computed, if possible, even
     * for unlabelled data.
     */
    final static ScorerInfo SCORE_UNLABELLED = new UnlabelledScorerInfo() {};

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
    static boolean scoreUnlabelled(AuxiliaryInfo... info) {
        for (AuxiliaryInfo infoObj : info) {
            if (infoObj instanceof UnlabelledScorerInfo) return true;
        }
        return false;
    }

}
