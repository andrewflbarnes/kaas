package net.aflb.kaas.core.legacy.data;

/**
 * <p>
 * Object containing details of Race Controls. Fields are analogous with the database.
 * </p>
 *
 * @author Barnesly
 */
public class RaceControl {
    private int controlId;
    private String league;
    private String date;

    /**
     * @return the control ID
     */
    public int getControlId() {
        return controlId;
    }

    /**
     * @param controlId
     *            the control ID to set
     */
    public void setControlId(int controlId) {
        this.controlId = controlId;
    }

    /**
     * @return the division
     */
    public String getLeague() {
        return league;
    }

    /**
     * @param division the division to set
     */
    public void setLeague(String division) {
        this.league = division;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "date " + this.date + ", league " + this.league + ", id " + this.controlId;
    }

}
