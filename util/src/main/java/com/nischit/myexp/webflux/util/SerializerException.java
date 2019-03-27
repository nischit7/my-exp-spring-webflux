/*
 * Project GreenBox
 * (c) 2015-2018 VMware, Inc. All rights reserved.
 * VMware Confidential.
 */

package com.nischit.myexp.webflux.util;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ResponseStatus(INTERNAL_SERVER_ERROR)
public class SerializerException extends RuntimeException {

    public SerializerException(final Throwable throwable) {
        super(throwable);
    }
}
