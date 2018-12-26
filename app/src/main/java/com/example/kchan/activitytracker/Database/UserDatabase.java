package com.example.kchan.activitytracker.Database;

import com.example.kchan.activitytracker.Singleton.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDatabase {
    UserInfo userInfo = new UserInfo();
    private DatabaseReference mDatabase;
    private String email, name;

    public UserDatabase(String email, String name)
    {
        this.email = email;
        this.name = name;
    }
    public UserDatabase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("UserDB");
    }

    public void storeUser(User user)
    {
        String googleUserID = user.getAccount().getId();
        if(googleUserID != null)
        {
            userInfo.setEmail(user.getAccount().getEmail());
            userInfo.setName(user.getAccount().getDisplayName());
            mDatabase.child(googleUserID).setValue(userInfo);
        }
    }
}
