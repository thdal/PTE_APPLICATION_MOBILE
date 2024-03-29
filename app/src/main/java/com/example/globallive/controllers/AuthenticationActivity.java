package com.example.globallive.controllers;

import android.content.Intent;
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
import com.example.globallive.threads.AuthenticateThread;
import com.example.globallive.threads.IAuthenticateActivityCallback;

public class AuthenticationActivity extends MainActivity implements View.OnClickListener, IAuthenticateActivityCallback{
    Button authenticationButton;
    TextView registerTextView;
    Button inviteButton;
    AuthenticateThread _thread;
    private IUserService _userService;
    private OperationSuccess _authenticationSuccess;

    private Handler _mainHandler = new Handler();

    public static void displayActivity(MainActivity activity){
        Intent intent = new Intent(activity, AuthenticationActivity.class);
        activity.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        setTitle("Connexion");

        _userService = new UserServiceImplementation();
        _authenticationSuccess = null;

        authenticationButton = findViewById(R.id.seConnecter);
        registerTextView = findViewById(R.id.inscrire);
        inviteButton = findViewById(R.id.invite);
        authenticationButton.setOnClickListener(this);
        registerTextView.setOnClickListener(this);
        inviteButton.setOnClickListener(this);

        TextView email = findViewById(R.id.identifiant);
        TextView password = findViewById(R.id.mot_de_passe);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seConnecter:
                TextView email = findViewById(R.id.identifiant);
                TextView password = findViewById(R.id.mot_de_passe);

                User user = new User();
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());


                _thread = new AuthenticateThread(this, user, _userService);
                _thread.start();
                break;
            case R.id.inscrire:
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.invite:
                User visiteur = new User();
                visiteur.setFirstName("Visiteur");
                visiteur.setProfile_id(0);
                HomeActivity.displayActivity(AuthenticationActivity.this, visiteur, "");
                break;
        }
    }

    @Override
    public void callBackSuccess(User user) {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                HomeActivity.displayActivity(AuthenticationActivity.this, user, "");
                
            }
        });
    }

    @Override
    public void callBackFail(String message) {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView _errorDisplay = findViewById(R.id.errorLogin);
                _errorDisplay.setText(message);
            }
        });
    }
}
