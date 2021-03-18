package com.nischit.myexp.spring.async.services.impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TeamNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 6085684763017890015L;

}
