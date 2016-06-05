package com.athila.mvpweather.infrastructure.validator;

import com.athila.mvpweather.interactor.validator.CoordinateValidator;
import com.athila.mvpweather.interactor.validator.EmailValidator;
import com.athila.mvpweather.interactor.validator.PasswordValidator;

import org.junit.Test;

import java.util.regex.Pattern;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;

/**
 * Created by athila on 15/03/16.
 */
public class ValidatorsTest {

    @Test
    public void testEmailValidator() {
        EmailValidator emailValidator = new EmailValidator();
        // This is the same pattern used by the default implementation of EmailValidator
        Pattern emailPattern = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );
        emailValidator.setValidationPattern(emailPattern);

        assertFalse(emailValidator.isValid("invalid.com"));
        assertFalse(emailValidator.isValid("www.invalid.com"));
        assertFalse(emailValidator.isValid("invalid@invalid_*7.com"));
        assertFalse(emailValidator.isValid("invalid.invalid.com"));
        assertFalse(emailValidator.isValid("invalid@invalid@invalid.com"));
        assertTrue(emailValidator.isValid("valid@valid.co"));
    }

    @Test
    public void testPasswordValidator() {
        PasswordValidator passwordValidator = new PasswordValidator();

        // passwords must only be bigger than 2 chars
        assertFalse(passwordValidator.isValid("12"));
        assertFalse(passwordValidator.isValid("a1"));
        assertFalse(passwordValidator.isValid("a1"));
        assertTrue(passwordValidator.isValid("a1a"));
        assertTrue(passwordValidator.isValid("123"));
        assertTrue(passwordValidator.isValid("abc123_^5%$_"));
    }

    @Test
    public void testCoordinateValidator() {
        CoordinateValidator coordinateValidator = new CoordinateValidator();

        assertFalse(coordinateValidator.isValid("&8.98"));
        assertFalse(coordinateValidator.isValid("-"));
        assertFalse(coordinateValidator.isValid("trf"));
        assertFalse(coordinateValidator.isValid("y7"));
        assertTrue(coordinateValidator.isValid("56789.0"));
        assertTrue(coordinateValidator.isValid("56789"));
        assertTrue(coordinateValidator.isValid("-56789"));
        assertTrue(coordinateValidator.isValid("-56789.9874"));
    }
}
