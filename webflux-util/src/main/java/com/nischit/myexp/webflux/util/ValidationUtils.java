package com.nischit.myexp.webflux.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

/**
 * Validation utils.
 */
public final class ValidationUtils {

    private static final Validator VALIDATOR;

    static {
        final ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        VALIDATOR = factory.getValidator();
    }

    private ValidationUtils() {
        // Nothing to do
    }

    /**
     * Validate an object.
     *
     * @param o      The object to validate.
     * @param groups the group or list of groups targeted for validation
     * @param <T>    The type of object.
     */
    public static <T> void validate(final T o, final Class<?>... groups) {
        final Set<ConstraintViolation<T>> result = VALIDATOR.validate(o, groups);
        if (!result.isEmpty()) {
            throw new ConstraintViolationException(result);
        }
    }

    /**
     * Validate an object (no exception will be thrown).
     *
     * @param o      The object to validate.
     * @param groups the group or list of groups targeted for validation
     * @param <T>    The type of object.
     * @return true if the object is valid.
     */
    public static <T> boolean isValid(final T o, final Class<?>... groups) {
        final Set<ConstraintViolation<T>> result = VALIDATOR.validate(o, groups);
        return result.isEmpty();
    }
}
