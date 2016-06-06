package com.athila.cleansample.interactor.validator;

public class PasswordValidator implements Validator<String> {

    @Override
    public boolean isValid(String password) {
        return password.length() > 2;
    }
}
