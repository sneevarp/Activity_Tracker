package com.example.kchan.activitytracker.Database;

import com.example.kchan.activitytracker.Singleton.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDatabase {
    private DatabaseReference mDatabase;
    private String Email, Name;
    UserInfo UI = new UserInfo();
    private boolean returnBool = false;
    public UserDatabase(String Email, String Name)
    {
        this.Email = Email;
        this.Name = Name;
    }
    public UserDatabase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("UserDB");
    }
    public void writeUser()
    {

    }

    public void storeUser(User user)
    {
        String googleUserID = user.getAccount().getId();
        if(googleUserID != null)
        {
            UI.setEmail(user.getAccount().getEmail());
            UI.setName(user.getAccount().getDisplayName());
            mDatabase.child(googleUserID).setValue(UI);
        }
    }

}
