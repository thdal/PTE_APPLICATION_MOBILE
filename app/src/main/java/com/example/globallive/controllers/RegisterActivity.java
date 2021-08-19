package com.example.globallive.controllers;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.globallive.R;

import com.example.globallive.entities.OperationSuccess;
import com.example.globallive.entities.User;
import com.example.globallive.services.IUserService;
import com.example.globallive.services.UserServiceImplementation;
import com.example.globallive.tabs.HomeActivity;
import com.example.globallive.threads.IRegisterActivityCallback;
import com.example.globallive.threads.RegisterThread;

public class RegisterActivity extends MainActivity implements View.OnClickListener , IRegisterActivityCallback {
    private Button registerButton;
    private IUserService _userService;
    private OperationSuccess _registrationSuccess;
    private RegisterThread _thread;

    private Handler _mainHandler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        setTitle("Register");
        showBack();

        registerButton = findViewById(R.id.sinscrire);
        registerButton.setOnClickListener(this);

        this._userService = new UserServiceImplementation();
        this._registrationSuccess = null;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sinscrire:

                TextView firstname =  findViewById(R.id.Firstname);
                TextView lastname = findViewById(R.id.Lastname);
                TextView email =  findViewById(R.id.Email);
                TextView password = findViewById(R.id.Password);

                User user = new User();
                user.setFirstName(firstname.getText().toString());
                user.setLastName(lastname.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());
                user.setGenre_id(2);//Homme
                user.setProfile_id(1);//Organisateur
                user.setUserImg(0); // false pas d'image
                user.setIsBanned(0); //false

                _thread = new RegisterThread(this, user, _userService);
                _thread.start();
                break;
            case R.id.DejaClient:
                AuthenticationActivity.displayActivity(RegisterActivity.this);
                break;
        }
    }

    @Override
    public void callBackSuccess(int userId) {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                HomeActivity.displayActivity(RegisterActivity.this, userId, "");
            }
        });

    }

    @Override
    public void callBackFail(String message) {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView _errorDisplay = findViewById(R.id.errorRegister);
                _errorDisplay.setText(message);
            }
        });
    }
}
