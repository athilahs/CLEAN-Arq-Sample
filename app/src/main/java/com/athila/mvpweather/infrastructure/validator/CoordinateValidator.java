package com.athila.mvpweather.infrastructure.validator;

public class CoordinateValidator implements Validator<String> {

    @Override
    public boolean isValid(String text) {
        // cordinates just need to be a double
        try {
            //noinspection ResultOfMethodCallIgnored
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
