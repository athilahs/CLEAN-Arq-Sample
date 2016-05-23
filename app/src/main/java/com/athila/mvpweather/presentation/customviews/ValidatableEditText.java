package com.athila.mvpweather.presentation.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.athila.mvpweather.infrastructure.validator.ValidationException;
import com.athila.mvpweather.infrastructure.validator.Validator;

public class ValidatableEditText extends EditText {

    protected Validator<String> mValidator;
    protected Context mContext;
    protected String mErrorMessage;

    protected ValidatableEditText(Context context) {
        super(context);
        mContext = context;
    }

    protected ValidatableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    protected boolean isInputValid() {
        return mValidator != null && mValidator.isValid(getText().toString());
    }

    public void setValidator(Validator<String> validator) {
        mValidator = validator;
    }

    public void validate() throws ValidationException {
        if (!isInputValid()) {
            throw new ValidationException();
        }
    }
}
