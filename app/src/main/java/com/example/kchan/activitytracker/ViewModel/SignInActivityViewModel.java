package com.example.kchan.activitytracker.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.example.kchan.activitytracker.Database.UserDatabase;
import com.example.kchan.activitytracker.DetailsActivity;
import com.example.kchan.activitytracker.GoogleSignInClientValue;
import com.example.kchan.activitytracker.MapsActivity;
import com.example.kchan.activitytracker.SigninActivity;
import com.example.kchan.activitytracker.Singleton.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;


public class SignInActivityViewModel {

    public static final int RC_SIGN_IN = 1;
    private static final String TAG = "SIGNINVIEWMODEL" ;
    private GoogleSignInClientValue googleSigninClientValue;
    private String googleUserID;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

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

    public void updateUI(final Context context, GoogleSignInAccount account) {
      if(account != null) {
            googleSigninClientValue = new GoogleSignInClientValue(context);
            User.init(account);
            final User user = User.getInstance();
            user.setAccount(account);

            final UserDatabase userDB = new UserDatabase();
            googleUserID = user.getAccount().getId();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("UserDB");

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(googleUserID))
                    {
                        Log.e("Inside if","Found copy");
                        Intent intent = new Intent(context, MapsActivity.class);
                        context.startActivity(intent);
                    }
                    else
                    {
                        Log.e("Inside else","Did not find copy");
                        //userDB.storeUser(user);
                        Intent intent = new Intent(context, DetailsActivity.class);
                        context.startActivity(intent);

                        /*Intent intent = new Intent(context, MapsActivity.class);
                        context.startActivity(intent);*/
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

}
