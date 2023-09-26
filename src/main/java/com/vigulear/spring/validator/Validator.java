package com.vigulear.spring.validator;

public interface Validator {
    <T> boolean isValid(T object);
}
