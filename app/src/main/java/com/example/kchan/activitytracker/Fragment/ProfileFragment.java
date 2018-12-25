package com.example.kchan.activitytracker.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kchan.activitytracker.R;
import com.example.kchan.activitytracker.Singleton.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private User user;
    private TextView fullName;
    private ImageView imageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ProfileFragment newInstance(){
        return new ProfileFragment();
    }

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        user = User.getInstance();
        fullName = (TextView)view.findViewById(R.id.FullName);
        User user = User.getInstance();
        fullName.setText(user.getAccount().getDisplayName());

        imageView = (ImageView)view.findViewById(R.id.profileImage);

        if(user.getAccount().getPhotoUrl() != null)
        Glide.with(this).load(user.getAccount().getPhotoUrl().toString()).into(imageView);

        return view;
    }

}
