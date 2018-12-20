package com.example.kchan.activitytracker.viewModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.kchan.activitytracker.GoogleSignInClientValue;
import com.example.kchan.activitytracker.MapsActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SignInActivityViewModel {

    private static final String TAG = "SIGNINVIEWMODEL" ;
    private GoogleSignInClientValue googleSigninClientValue;
    public static final int RC_SIGN_IN = 1;

    public SignInActivityViewModel() {
    }

    public void handleSignInResult(Context context, Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(context,account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(context,null);
        }
    }

    public void updateUI(Context context, GoogleSignInAccount account) {
        if(account == null){

        }else {
            googleSigninClientValue = new GoogleSignInClientValue(context);
            Intent intent = new Intent(context, MapsActivity.class);
            context.startActivity(intent);
        }
    }
}
