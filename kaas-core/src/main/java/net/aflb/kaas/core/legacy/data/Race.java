package net.aflb.kaas.core.legacy.data;

/**
 * <p>
 * Object containing details of Races. Fields are analogous with the database.
 * </p>
 *
 * @author Barnesly
 */
public class Race implements Comparable<Race> {
    private static final String[] groups = { "A", "E", "B", "F", "C", "G", "D",
            "H", "I", "II", "III", "IV", "V", "VI", "VII", "VIII" };
    private int controlId;
    private String division;
    private int roundNo;
    private int raceNo;
    private String group;
    private int teamOne;
    private int teamTwo;
    private int teamWin;
    private String teamOneDSQ;
    private String teamTwoDSQ;

    /**
     * Standard constructor
     */
    public Race() {
        // Empty default constructor
    }
    // FIXME? Android Parcelable
//    /**
//     * {@link Parcelable} constructor
//     *
//     * @param in Create a new instance using a {@link Parcel}
//     */
//    public Race(Parcel in) {
//        this.controlId = in.readInt();
//        this.division = in.readString();
//        this.roundNo = in.readInt();
//        this.raceNo = in.readInt();
//        this.group = in.readString();
//        this.teamOne = in.readInt();
//        this.teamTwo = in.readInt();
//        this.teamWin = in.readInt();
//        this.teamOneDSQ = in.readString();
//        this.teamTwoDSQ = in.readString();
//    }

    /**
     * @return the control ID this race is being run under
     */
    public int getControlId() {
        return controlId;
    }

    /**
     * @param controlId
     *            the control ID this race is being run under
     */
    public void setControlId(int controlId) {
        this.controlId = controlId;
    }

    /**
     * @return the division this race is being run under
     */
    public String getDivision() {
        return division;
    }

    /**
     * @param division
     *            the division this race is being run under
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * @return the round number this race is being run under
     */
    public int getRoundNo() {
        return roundNo;
    }

    /**
     * @param roundNo
     *            the round number this race is being run under
     */
    public void setRoundNo(int roundNo) {
        this.roundNo = roundNo;
    }

    /**
     * @return the race number of this race
     */
    public int getRaceNo() {
        return raceNo;
    }

    /**
     * @param raceNo
     *            the race number of this race
     */
    public void setRaceNo(int raceNo) {
        this.raceNo = raceNo;
    }

    /**
     * @return the group name this race is being run under
     */
    public String getGroup() {
        return group;
    }

    /**
     * @param group the group name this race is being run under
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * @return the team ID of team 1
     */
    public int getTeamOne() {
        return teamOne;
    }

    /**
     * @param teamOne
     *            the team ID of team 1
     */
    public void setTeamOne(int teamOne) {
        this.teamOne = teamOne;
    }

    /**
     * @return the team ID of team 2
     */
    public int getTeamTwo() {
        return teamTwo;
    }

    /**
     * @param teamTwo
     *            the team ID of team 2
     */
    public void setTeamTwo(int teamTwo) {
        this.teamTwo = teamTwo;
    }

    /**
     * @return 1 if team 1 has won, 2 if team 2 has one, 0 if neither has won
     */
    public int getTeamWin() {
        return teamWin;
    }

    /**
     * @param teamWin
     *            1 if team 1 has won, 2 if team 2 has one, 0 if neither has won
     */
    public void setTeamWin(int teamWin) {
        this.teamWin = teamWin;
    }

    /**
     * @return empty or null if team one has not been disqualified, otherwise
     *         the text associated with the disqualification
     */
    public String getTeamOneDSQ() {
        return teamOneDSQ;
    }

    /**
     * @param teamOneDSQ
     *            empty or null if team one has not been disqualified, otherwise
     *            the text associated with the disqualification
     */
    public void setTeamOneDSQ(String teamOneDSQ) {
        this.teamOneDSQ = teamOneDSQ;
    }

    /**
     * @return the empty or null if team two has not been disqualified, otherwise
     *         the text associated with the disqualification
     */
    public String getTeamTwoDSQ() {
        return teamTwoDSQ;
    }

    /**
     * @param teamTwoDSQ
     *            the empty or null if team two has not been disqualified,
     *            otherwise the text associated with the disqualification
     */
    public void setTeamTwoDSQ(String teamTwoDSQ) {
        this.teamTwoDSQ = teamTwoDSQ;
    }

    // FIXME
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
//    @SuppressLint("DefaultLocale")
    @Override
    public int compareTo(/* @NonNull */ Race another) {
        // Order by the division, then group ID, then race number finally

        int check = this.division.toUpperCase().toCharArray()[0] - another.division.toUpperCase().toCharArray()[0];

        if (check == 0) {
            // Find ordinal version of the group
            int ordThis = 0;
            int ordAnother = 0;
            for (int i = 0, n = groups.length; i < n; i++) {
                if (this.group.equalsIgnoreCase(groups[i])) {
                    ordThis = i;
                }
                if (another.group.equalsIgnoreCase(groups[i])) {
                    ordAnother = i;
                }
            }
            check = ordThis - ordAnother;
        }
        if (check == 0) {
            check = this.raceNo - another.raceNo;
        }

        return check;
    }

    // FIXME? Android Parcelable
//    /* (non-Javadoc)
//     * @see android.os.Parcelable#describeContents()
//     */
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    /* (non-Javadoc)
//     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
//     */
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(controlId);
//        dest.writeString(division);
//        dest.writeInt(roundNo);
//        dest.writeInt(raceNo);
//        dest.writeString(group);
//        dest.writeInt(teamOne);
//        dest.writeInt(teamTwo);
//        dest.writeInt(teamWin);
//        dest.writeString(teamOneDSQ);
//        dest.writeString(teamTwoDSQ);
//    }
//
//    /**
//     * Instantiates the necessary {@code static} {@link Parcelable.Creator}
//     */
//    public static final Parcelable.Creator<Race> CREATOR = new Parcelable.Creator<Race>() {
//
//        /* (non-Javadoc)
//         * @see android.os.Parcelable.Creator#createFromParcel(android.os.Parcel)
//         */
//        @Override
//        public Race createFromParcel(Parcel source) {
//            return new Race(source);
//        }
//
//        /* (non-Javadoc)
//         * @see android.os.Parcelable.Creator#newArray(int)
//         */
//        @Override
//        public Race[] newArray(int size) {
//            return new Race[size];
//        }
//
//    };

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "id " + this.controlId + ", division " + this.division + ", set "
                + this.roundNo + ", race " + this.raceNo + ", team " + this.teamOne + " vs team " + this.teamTwo;
    }
}
