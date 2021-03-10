package net.aflb.kaas.core.spi;

import net.aflb.kaas.core.model.competing.Match;

import java.io.Serial;

/**
 * Unchecked exception thrown when a {@link Match} object has not been run.
 */
public class RaceNotRunException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * String constructor
     *
     * @param reason the {@link String} description of the cause
     */
    public RaceNotRunException(String reason) {
        super(reason);
    }
}
