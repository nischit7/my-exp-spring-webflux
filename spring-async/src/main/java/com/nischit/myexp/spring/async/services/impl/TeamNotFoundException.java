package com.nischit.myexp.spring.async.services.impl;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class TeamNotFoundException extends RuntimeException {

}
