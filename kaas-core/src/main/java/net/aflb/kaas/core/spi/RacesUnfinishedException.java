package net.aflb.kaas.core.spi;

import java.io.Serial;

/**
 * Checked exception thrown when we try and process matches for
 * the next set but there are unfinished/unrun races
 */
public class RacesUnfinishedException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public RacesUnfinishedException(String reason) {
        super(reason);
    }

    public RacesUnfinishedException(Throwable t) {
        super(t);
    }
}
