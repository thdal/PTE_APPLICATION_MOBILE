package com.example.globallive;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*import com.example.globallive.OperationSuccess;
import com.example.globallive.Person;
import com.example.globallive.IUserService;
import com.example.globallive.UserServiceImplementation;
import com.example.globallive.AuthenticateThread;
import com.example.globallive.IAuthenticateActivityCallback;*/

public class AuthenticationActivity extends MainActivity implements View.OnClickListener /*, IAuthenticateActivityCallback */{
    Button authenticationButton;
    TextView registerTextView;
    Button inviteButton;
  //  AuthenticateThread _thread;
   // private IUserService _userService;
   // private OperationSuccess _authenticationSuccess;

    private Handler _mainHandler = new Handler();

    public static void displayActivity(MainActivity activity){
        Intent intent = new Intent(activity, com.example.globallive.AuthenticationActivity.class);
        activity.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        setTitle("Authenticate");

       // _userService = new UserServiceImplementation();
       // _authenticationSuccess = null;

        authenticationButton = findViewById(R.id.seConnecter);
        registerTextView = findViewById(R.id.inscrire);
        inviteButton = findViewById(R.id.invite);
        authenticationButton.setOnClickListener(this);
        registerTextView.setOnClickListener(this);
        inviteButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seConnecter:
                TextView username =  findViewById(R.id.identifiant);
                TextView password = findViewById(R.id.mot_de_passe);

             /*   Person person = new Person();
                person.setUsername(username.getText().toString());
                person.setPassword(password.getText().toString());

                _thread = new AuthenticateThread(this, person, _userService);
                _thread.start();
                break;*/
            case R.id.inscrire:
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
                break;

            case R.id.invite:
                Intent a = new Intent(this, HomeActivity.class);
                startActivity(a);
                break;
        }
    }

  /*  @Override
    public void callBackSuccess(int userID) {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                HomeActivity.displayActivity(com.example.globallive.AuthenticationActivity.this);
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
    }*/
}
