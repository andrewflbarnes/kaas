package net.aflb.kaas.core.legacy.races.engine;

/**
 * Exception thrown when an error occurs trying to generate this set of races
 *
 * @author Barnesly
 */
public class RaceGenerationFailException extends Exception {
    private static final long serialVersionUID = 1L;

    public RaceGenerationFailException(Throwable throwable) {
        super(throwable);
    }

    public RaceGenerationFailException(String detailMessage) {
        super(detailMessage);
    }
}
