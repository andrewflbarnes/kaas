package net.aflb.kaas.core.spi;

import net.aflb.kaas.core.model.competing.Match;

import java.io.Serial;

/**
 * Unchecked exception thrown when a {@link Match} object cannot be retrieved
 * or found
 */
public class RaceNotFoundException extends IllegalStateException {
    @Serial
    private static final long serialVersionUID = 1L;

    public RaceNotFoundException(String reason) {
        super(reason);
    }

    public RaceNotFoundException(Throwable t) {
        super(t);
    }
}
