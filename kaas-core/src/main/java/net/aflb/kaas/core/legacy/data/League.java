package net.aflb.kaas.core.legacy.data;


/**
 * <p>
 * Central location for storing the defined leagues since they are not stored in
 * the database.
 * </p>
 *
 * <p>
 * These must match entries exactly as they appear in pref_league_values of
 * strings.xml in order for settings to work. I will try and fix this at some
 * point
 * </p>
 *
 * @author Barnesly
 */
public class League {
    /*
     * Note that League.league has only been used in specific scenarios
     * requiring it and not all scnarios which could use it
     */
    public static final String NORTHERN = "Northern";
    public static final String SOUTHERN = "Southern";
    public static final String MIDLANDS = "Midlands";
    public static final String WESTERN = "Western";

    public static final String[] ALL_LEAGUES = { NORTHERN, SOUTHERN, MIDLANDS, WESTERN };
}
