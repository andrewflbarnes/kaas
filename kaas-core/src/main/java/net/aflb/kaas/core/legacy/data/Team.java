package net.aflb.kaas.core.legacy.data;

/**
 * <p>
 * Object containing details of Teams. Fields are analogous with the database
 * but also contain the ordered scores for comparison purposes.
 * </p>
 *
 * @author Barnesly
 */
public class
Team implements Comparable<Team> {
    private int teamId;
    private String league;
    private String clubName;
    private String division;
    private int divisionIndex;
    private String teamName;
    private int scoreR1;
    private int scoreR2;
    private int scoreR3;
    private int scoreR4;
    private int scoreR5;
    private int scoreTotal;
    private int orderedScore1;
    private int orderedScore2;
    private int orderedScore3;
    private int orderedScore4;
    private int orderedScore5;
    /*
     * Dynamic variables used for race processing
     */
    private int setOneWins;
    private int setOneDsqs;
    private int setWeighting;

    /**
     * @return the team id
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     * @param teamId the team id to set
     */
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    /**
     * @return the league
     */
    public String getLeague() {
        return league;
    }

    /**
     * @param league the league to set
     */
    public void setLeague(String league) {
        this.league = league;
    }

    /**
     * @return the club name
     */
    public String getClubName() {
        return clubName;
    }

    /**
     * @param clubName
     *            the club name to set
     */
    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    /**
     * @return the division
     */
    public String getDivision() {
        return division;
    }

    /**
     * @param division
     *            the division to set
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * @return the team's order within the division for the club
     */
    public int getDivisionIndex() {
        return divisionIndex;
    }

    /**
     * @param divisionIndex
     *            the team's order within the division for the club
     */
    public void setDivisionIndex(int divisionIndex) {
        this.divisionIndex = divisionIndex;
    }

    /**
     * @return the team name
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * @param teamName
     *            the team name to set
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * @return the team's score from round 1
     */
    public int getScoreR1() {
        return scoreR1;
    }

    /**
     * @param scoreR1
     *            the team's score from round 1
     */
    public void setScoreR1(int scoreR1) {
        this.scoreR1 = scoreR1;
        updateScores();
    }

    /**
     * @return the team's score from round 2
     */
    public int getScoreR2() {
        return scoreR2;
    }

    /**
     * @param scoreR2
     *            the team's score from round 2
     */
    public void setScoreR2(int scoreR2) {
        this.scoreR2 = scoreR2;
        updateScores();
    }

    /**
     * @return the team's score from round 3
     */
    public int getScoreR3() {
        return scoreR3;
    }

    /**
     * @param scoreR3
     *            the team's score from round 3
     */
    public void setScoreR3(int scoreR3) {
        this.scoreR3 = scoreR3;
        updateScores();
    }

    /**
     * @return the team's score from round 4
     */
    public int getScoreR4() {
        return scoreR4;
    }

    /**
     * @param scoreR4
     *            the team's score from round 4
     */
    public void setScoreR4(int scoreR4) {
        this.scoreR4 = scoreR4;
        updateScores();
    }

    /**
     * @return the team's score from round 5
     */
    public int getScoreR5() {
        return scoreR5;
    }

    /**
     * @param scoreR5
     *            the team's score from round 5
     */
    public void setScoreR5(int scoreR5) {
        this.scoreR5 = scoreR5;
        updateScores();
    }

    /**
     * @return the total combined score for all 5 rounds
     */
    public int getScoreTotal() {
        return scoreTotal;
    }

    // TODO Add method to returned the ordered score with an integer parameter.

    /**
     * @return the team's highest score from all 5 rounds
     */
    public int getOrderedScore1() {
        return orderedScore1;
    }

    /**
     * @return the team's second highest score from all 5 rounds
     */
    public int getOrderedScore2() {
        return orderedScore2;
    }

    /**
     * @return the team's third highest score from all 5 rounds
     */
    public int getOrderedScore3() {
        return orderedScore3;
    }

    /**
     * @return the team's fourth highest score from all 5 rounds
     */
    public int getOrderedScore4() {
        return orderedScore4;
    }

    /**
     * @return the team's fifth highest score from all 5 rounds
     */
    public int getOrderedScore5() {
        return orderedScore5;
    }

    /**
     * @param rounds
     *            the number of rounds to total
     * @return the total of the highest scores for the number of rounds
     *         specified
     */
    public int getOrderedTotal(int rounds) {
        int total = 0;
        switch (rounds) {
            case 5:
                total += this.orderedScore5;
            case 4:
                total += this.orderedScore4;
            case 3:
                total += this.orderedScore3;
            case 2:
                total += this.orderedScore2;
            case 1:
                total += this.orderedScore1;
            default:
        }
        return total;
    }

    /**
     * @return the number of races this team won in set one
     */
    public int getSetOneWins() {
        return setOneWins;
    }

    /**
     * @param setOneWins
     *            the number of races this team won in set one
     */
    public void setSetOneWins(int setOneWins) {
        this.setOneWins = setOneWins;
    }

    /**
     * @return the number of races this team got DSQs for in set one
     */
    public int getSetOneDsqs() {
        return setOneDsqs;
    }

    /**
     * @param setOneDsqs
     *            the number of races this team got DSQs for in set one
     */
    public void setSetOneDsqs(int setOneDsqs) {
        this.setOneDsqs = setOneDsqs;
    }

    /**
     * @return the weighting of this teams success
     */
    public int getSetWeighting() {
        return setWeighting;
    }

    /**
     * @param setWeighting the weighting of the teams success to set
     */
    public void setSetWeighting(int setWeighting) {
        this.setWeighting = setWeighting;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
//		return "(" + getOrderedTotal(5) + ") " + teamName + " - " + clubName + " "
        // + division + " " + divisionIndex;
        return "total " + this.getOrderedTotal(5) + ", team " + this.teamName
                + ", club " + this.clubName + ", division " + this.division
                + " " + this.divisionIndex;
    }

    /**
     * @return alternative, cut down version of {@link #toString}.
     */
    public String infoToString() {
        return this.clubName + " " + this.division + " team " + this.divisionIndex;
    }

    /**
     * Updates the internal ordered scores, this is called whenever a round
     * score is set
     */
    private void updateScores() {
        int tempScore;
        int[] scores = new int[5];
        scores[0] = this.scoreR1;
        scores[1] = this.scoreR2;
        scores[2] = this.scoreR3;
        scores[3] = this.scoreR4;
        scores[4] = this.scoreR5;

        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 5; j++) {
                if (scores[i] < scores[j]) {
                    tempScore = scores[i];
                    scores[i] = scores[j];
                    scores[j] = tempScore;
                }
            }
        }

        this.orderedScore1 = scores[0];
        this.orderedScore2 = scores[1];
        this.orderedScore3 = scores[2];
        this.orderedScore4 = scores[3];
        this.orderedScore5 = scores[4];

        this.scoreTotal = this.scoreR1 + this.scoreR2 + this.scoreR3 + this.scoreR4 + this.scoreR5;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
//    @SuppressLint("DefaultLocale")
    @Override
    public int compareTo(/* @NonNull */ Team another) {
        int check;

        // Largest first so reverse the compare
        check = another.getScoreTotal() - this.getScoreTotal();

        // If the total scores are equal check on a round by round basis
        if (check == 0) {
            check = another.getOrderedScore1() - this.getOrderedScore1();
        }
        if (check == 0) {
            check = another.getOrderedScore2() - this.getOrderedScore2();
        }
        if (check == 0) {
            check = another.getOrderedScore3() - this.getOrderedScore3();
        }
        if (check == 0) {
            check = another.getOrderedScore4() - this.getOrderedScore4();
        }
        if (check == 0) {
            check = another.getOrderedScore5() - this.getOrderedScore5();
        }
        // Next checks are the normal way round as we want non seeded teams to
        // be ordered alphabetically
        if (check == 0) {
            check = this.getClubName().toUpperCase().compareTo(another.getClubName().toUpperCase());
        }
        return check;
    }

}
