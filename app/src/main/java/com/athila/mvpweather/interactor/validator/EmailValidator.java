package com.athila.mvpweather.interactor.validator;

import android.support.annotation.VisibleForTesting;

import java.util.regex.Pattern;

import static android.util.Patterns.EMAIL_ADDRESS;

public class EmailValidator implements Validator<String> {

    private Pattern mValidationPattern;

    public EmailValidator() {
        super();
        mValidationPattern = EMAIL_ADDRESS;
    }

    @Override
    public boolean isValid(String email) {
        return mValidationPattern.matcher(email).matches();
    }

    @VisibleForTesting
    public void setValidationPattern(Pattern validationPattern) {
        mValidationPattern = validationPattern;
    }
}
