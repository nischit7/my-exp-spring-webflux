package org.example.webflux.localization;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

/**
 * Class for providing locale specific messages and error messages.
 * The message sources are initialized in WebConfig class.
 */
@Component
public class Messages {

    public static final Locale DEFAULT_LOCALE = Locale.US;
    public static final String SERVER_ERROR_KEY = "unmapped.message";
    public static final String BAD_REQUEST_ERROR = "bad.payload";
    public static final String UNSUPPORTED_MEDIA_TYPE_ERROR = "media.type.unsupported";
    public static final String UNSUPPORTED_ACCEPT_HEADER_ERROR = "accept.header.unsupported";
    private static final String MSG_KEY_NOT_FOUND = "Message Key Not Found";
    private static final ThreadLocal<Locale> TTL_LOCALE = new ThreadLocal<>();

    private MessageSource errorMessageSource;

    private MessageSource messageSource;

    @Autowired
    public Messages(
            @Qualifier("errorMessageSource") final MessageSource errorMessageSource,
            @Qualifier("messageSource") final MessageSource messageSource) {

        this.errorMessageSource = errorMessageSource;
        this.messageSource = messageSource;
    }

    /**
     * Api for getting error message in specific locale.
     * Checks if the error message source contains given key in the specific locale.
     * If not found, checks it in default locale.
     * If not found, returns MSG_KEY_NOT_FOUND.
     *
     * @param msgKey The mesage key.
     * @param args All the arguments for message to be composed.
     * @return String The localized error message.
     */
    public String getLocalizedErrorMessage(final String msgKey, final Object... args) {
        return getLocalizedMessage(errorMessageSource, msgKey, args, getLocale());
    }

    /**
     * Api for getting error message in default locale.
     *
     * @param msgKey The mesage key.
     * @param args All the arguments for message to be composed.
     * @return String The localized error message.
     */
    public String getErrorMessage(final String msgKey, final Object... args) {
        return getLocalizedMessage(errorMessageSource, msgKey, args, DEFAULT_LOCALE);
    }

    /**
     * Api for getting  message in specific locale.
     * Checks if the message source contains given key in the specific locale.
     * If not found, checks it in default locale.
     * If not found, returns MSG_KEY_NOT_FOUND.
     *
     * @param msgKey The mesage key.
     * @param args All the arguments for message to be composed.
     * @return String The localized error message.
     */
    public String getLocalizedMessage(final String msgKey, final Object... args) {
        return getLocalizedMessage(messageSource, msgKey, args, getLocale());

    }

    /**
     * Api for getting  message in specific locale.
     * Checks if the message source contains given key in the specific locale.
     * If not found, checks it in default locale.
     * If not found, returns MSG_KEY_NOT_FOUND.
     *
     * @param msgKey The mesage key.
     * @param args All the arguments for message to be composed.
     * @return String The localized error message.
     */
    public String getLocalizedErrorMessage(final String msgKey, final Locale locale, final Object... args) {
        return getLocalizedMessage(errorMessageSource, msgKey, args, locale);
    }

    /**
     * Api for getting  message in default locale.
     *
     * @param msgKey The mesage key.
     * @param args All the arguments for message to be composed.
     * @return String The localized error message.
     */
    public String getMessage(final String msgKey, final Object... args) {
        return getLocalizedMessage(messageSource, msgKey, args, DEFAULT_LOCALE);
    }

    /**
     * Sets the locale in thread locale variable.
     *
     * @param locale An instance of {@link Locale}.
     */
    public static void setLocale(final Locale locale) {
        TTL_LOCALE.set(locale);
    }

    /**
     * Returns the current locale.
     *
     * @return An instance of {@link Locale}.
     */
    public static Locale getLocale() {
        final Locale locale = TTL_LOCALE.get();
        return (locale == null) ? DEFAULT_LOCALE : locale;
    }

    /**
     * Checks if a message source contains given key in the specific locale.
     * If not found, checks it in default locale.
     * If not found, returns MSG_KEY_NOT_FOUND.
     *
     * @param messageSource An instance of {@link MessageSource}.
     * @param msgKey The mesage key.
     * @param args All the arguments for message to be composed.
     * @param locale An instance of {@link Locale}.
     * @return String The localized error message.
     */
    private String getLocalizedMessage(
            final MessageSource messageSource,
            final String msgKey,
            final Object[] args,
            final Locale locale) {

        String localizedMessage = null;
        try {
            localizedMessage = messageSource.getMessage(msgKey, args, locale);
        } catch (final NoSuchMessageException e) {
            if (!locale.equals(DEFAULT_LOCALE)) {
                try {
                    localizedMessage = messageSource.getMessage(msgKey, args, DEFAULT_LOCALE);
                } catch (final NoSuchMessageException ex) {
                    localizedMessage = MSG_KEY_NOT_FOUND;
                }
            }
        }
        return localizedMessage;
    }

}
