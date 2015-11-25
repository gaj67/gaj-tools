package gaj.afl.data.finalsiren;

import gaj.afl.data.core.Fixture;
import gaj.afl.data.core.Match;
import gaj.afl.data.core.Outcome;
import gaj.afl.data.core.Scores;

/*package-private*/class MatchImpl implements Match {

    private final Fixture fixture;
    private final Scores homeScores;
    private final Scores awayScores;
    private final Outcome result;

    /* package-private */MatchImpl(Fixture fixture, Scores homeScores, Scores awayScores,
            Outcome result) {
        this.fixture = fixture;
        this.homeScores = homeScores;
        this.awayScores = awayScores;
        this.result = result;
    }

    @Override
    public Fixture getFixture() {
        return fixture;
    }

    @Override
    public Scores getHomeTeamScores() {
        return homeScores;
    }

    @Override
    public Scores getAwayTeamScores() {
        return awayScores;
    }

    @Override
    public Outcome getOutcome() {
        return result;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("Fixture: ");
        buf.append(fixture.toString());
        buf.append(", ");
        buf.append("Home-scores: ");
        buf.append(homeScores.toString());
        buf.append(", ");
        buf.append("Away-scores: ");
        buf.append(awayScores.toString());
        buf.append(", ");
        buf.append("Outcome: ");
        buf.append(result.toString());
        return buf.toString();
    }
}
