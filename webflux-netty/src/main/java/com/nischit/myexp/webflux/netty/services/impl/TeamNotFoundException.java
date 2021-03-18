package com.nischit.myexp.webflux.netty.services.impl;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class TeamNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 8020697832539492555L;

}
