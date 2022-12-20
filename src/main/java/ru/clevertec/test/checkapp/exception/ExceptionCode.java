package ru.clevertec.test.checkapp.exception;

import lombok.ToString;

@ToString
public enum ExceptionCode {
    ENTITY_NOT_FOUND(1000,"Expected Entity was not found"),
    ENTITY_NOT_VALID(1001,"Entity not valid as required"),
    ENTITY_ALREADY_EXIST(1002, "Entity already exist"),
    ID_LIST_IS_EMPTY(1003, "Id list is empty");
    private int code;
    private String message;
    ExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
