package org.example.webflux.localization;

/**
 * Base exception class which will be extended by other Exception classes.
 * Allows passing of variable args which can be utilized for localizing messages by {@link Messages}.
 */
public class LocalizationParamValueException extends RuntimeException {

    private static final long serialVersionUID = 141122624052332484L;

    private final Object[]  args;

    public LocalizationParamValueException(final Object... args) {
        this.args = args;
    }

    public LocalizationParamValueException(final Throwable throwable, final Object... args) {
        super(throwable);
        this.args = args;
    }

    /**
     * Returns all the exception related args.
     *
     * @return An array of arguments initialized.
     */
    public Object[] getArgs() {
        return args;
    }
}
