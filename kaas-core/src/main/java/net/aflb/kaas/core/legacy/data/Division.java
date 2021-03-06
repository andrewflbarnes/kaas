/**
 *
 */
package net.aflb.kaas.core.legacy.data;

/**
 * Simple class used as a central place for referencing each division. Division
 * are not currently stored in the database hence why they are set here.
 *
 * @author Barnesly
 */
public class Division {
    public static final String MIXED = "Mixed";
    public static final String LADIES = "Ladies";
    public static final String BOARD = "Board";

    public static final String[] ALL_DIVISIONS = { MIXED, LADIES, BOARD };

    // TODO Start using this kind of code? Need some kind of static initialiser
    // thougb to pull out the values from strings.xml
    /**
     * <b>Don't use these!</b> They have not been implemented anywhere.
     *
     * @author Barnesly
     */
    public enum DIVISIONS {
        MIXED("Mixed"),
        LADIES("Ladies"),
        BOARD("Board");

        private String value;

        DIVISIONS(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String s) {
            this.value = s;
        }
    }
}
