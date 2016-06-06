package com.athila.cleansample.presentation.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.athila.cleansample.R;
import com.athila.cleansample.interactor.validator.ValidationException;

/**
 * Created by athila on 10/03/16.
 */
public class ShakerEditText extends ValidatableEditText {
    public ShakerEditText(Context context) {
        super(context);
    }

    public ShakerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ValidatableEditText,
                0, 0);

        try {
            mErrorMessage = a.getString(R.styleable.ValidatableEditText_errorMessage);
        } finally {
            a.recycle();
        }
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    public void setErrorMessage(@StringRes int errorMessageId) {
        mErrorMessage = mContext.getString(errorMessageId);
    }

    @Override
    public void validate() throws ValidationException {
        if (!isInputValid()) {
            Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
            startAnimation(shake);
            setError(mErrorMessage);

            // throws exception so the execution can be interrupted when validation fails
            throw new ValidationException();
        }
    }
}
