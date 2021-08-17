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
import com.example.globallive.threads.IRegisterActivityCallback;
import com.example.globallive.threads.RegisterThread;

public class FormEventActivity extends MainActivity implements View.OnClickListener , IRegisterActivityCallback {
    private Button submitEventButton;
    private OperationSuccess _registrationSuccess;
    private RegisterThread _thread;

    private Handler _mainHandler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_event);
        setTitle("Formulaire d'événement");
        showBack();

        submitEventButton = findViewById(R.id.submitEvent);
        submitEventButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.submitEvent:

                break;
        }
    }

    @Override
    public void callBackSuccess(int userId) {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //HomeActivity.displayActivity(FormEventActivity.this, userId, "");
            }
        });

    }

    @Override
    public void callBackFail(String message) {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //TextView _errorDisplay = findViewById(R.id.errorRegister);
                //_errorDisplay.setText(message);
            }
        });
    }
}
