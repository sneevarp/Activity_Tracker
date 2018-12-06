package com.example.kchan.activitytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.kchan.activitytracker.viewModel.SignInActivityViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SIGNINACTIVITY";
    private GoogleSignInClientValue googleSigninClientValue;
    private SignInActivityViewModel signInActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        googleSigninClientValue = new GoogleSignInClientValue(this);
        signInActivityViewModel = new SignInActivityViewModel();

        //register the buttons
        findViewById(R.id.sign_in_button).setOnClickListener((View.OnClickListener)this);

    }

    private void signIn() {
        Intent signInIntent = googleSigninClientValue.getInstance().getSignInIntent();
        startActivityForResult(signInIntent, signInActivityViewModel.RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        signInActivityViewModel.updateUI(this,account);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == signInActivityViewModel.RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            signInActivityViewModel.handleSignInResult(this,task);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }
}
