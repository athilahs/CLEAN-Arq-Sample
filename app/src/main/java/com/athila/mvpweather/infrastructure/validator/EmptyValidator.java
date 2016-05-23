package com.athila.mvpweather.infrastructure.validator;

public class EmptyValidator implements Validator<String> {

    @Override
    public boolean isValid(String text) {
        return text != null && text.length() > 0;
    }
}
