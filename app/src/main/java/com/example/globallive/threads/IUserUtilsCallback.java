package com.example.globallive.threads;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.globallive.R;
import com.example.globallive.entities.EventCanal;
import com.example.globallive.entities.UserProfil;

import java.util.ArrayList;
import java.util.List;

public interface IUserUtilsCallback {
    void getUserProfilsCallback(List<UserProfil> userProfils); // would be in any signature


}

