package net.aflb.kaas.core.legacy.data;

/**
 * <p>
 * Object containing details of Clubs. Fields are analogous with the database.
 * </p>
 *
 * @author Barnesly
 */
public class Club {
    private String clubName;
    private String league;
    private String clubShortName;
    private int mixedTeams;
    private int ladiesTeams;
    private int boardTeams;

    /**
     * @return the clubName
     */
    public String getClubName() {
        return clubName;
    }

    /**
     * @param clubName
     *            the clubName to set
     */
    public void setClubName(String clubName) {
        this.clubName = clubName;
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
     * @return the number of mixed teams
     */
    public int getMixedTeams() {
        return mixedTeams;
    }

    /**
     * @param mixedTeams the number of mixed teams to set
     */
    public void setMixedTeams(int mixedTeams) {
        this.mixedTeams = mixedTeams;
    }

    /**
     * @return the number of ladies teams
     */
    public int getLadiesTeams() {
        return ladiesTeams;
    }

    /**
     * @param ladiesTeams the number of ladies teams to set
     */
    public void setLadiesTeams(int ladiesTeams) {
        this.ladiesTeams = ladiesTeams;
    }

    /**
     * @return the number of board teams
     */
    public int getBoardTeams() {
        return boardTeams;
    }

    /**
     * @param boardTeams the number of board teams to set
     */
    public void setBoardTeams(int boardTeams) {
        this.boardTeams = boardTeams;
    }

    /**
     * @return the clubShortName
     */
    public String getClubShortName() {
        return clubShortName;
    }

    /**
     * @param clubShortName
     *            the clubShortName to set
     */
    public void setClubShortName(String clubShortName) {
        this.clubShortName = clubShortName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (clubShortName == null || clubShortName.isEmpty()) {
            return this.clubName + " (" + this.league + ")";
        }

        return this.clubShortName + " (" + this.league + ")";
    }

}
