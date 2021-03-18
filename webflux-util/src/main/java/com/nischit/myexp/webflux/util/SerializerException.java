package com.nischit.myexp.webflux.util;

import org.example.webflux.localization.LocalizationParamValueException;
import org.example.webflux.localization.MsgKey;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ResponseStatus(INTERNAL_SERVER_ERROR)
@MsgKey("json.serialization.error")
public class SerializerException extends LocalizationParamValueException {

    private static final long serialVersionUID = -3311152483400136467L;

    public SerializerException(final Throwable throwable) {
        super(throwable);
    }
}
