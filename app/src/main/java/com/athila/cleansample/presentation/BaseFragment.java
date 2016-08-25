package com.athila.cleansample.presentation;


import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import com.athila.cleansample.R;

import java.net.UnknownHostException;


/**
 * Created by athila on 17/03/16.
 */
public abstract class BaseFragment extends Fragment implements BasicErrorsHandlerView {

    @Override
    public boolean handleBasicError(Throwable error) {
        View view = getView();
        if (view == null) {
            return false;
        }

        Snackbar errorMessage = Snackbar.make(view, R.string.error_generic, Snackbar.LENGTH_LONG);
        if (error instanceof UnknownHostException) {
            errorMessage.setText(R.string.error_no_internet_connection);
        }
        // TODO: add more generic error handling
        // default will show the most generic message
        errorMessage.show();
        return true;
    }
}
