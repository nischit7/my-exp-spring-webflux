package org.example.webflux.localization;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for exceptions.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MsgKey {
    String value();
}
