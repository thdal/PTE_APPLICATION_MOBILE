package com.example.globallive;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*import com.example.globallive.OperationSuccess;
import com.example.globallive.Person;
import com.example.globallive.IUserService;
import com.example.globallive.UserServiceImplementation;
import com.example.globallive.IRegisterActivityCallback;
import com.example.globallive.RegisterThread;*/

public class RegisterActivity extends MainActivity implements View.OnClickListener /*, IRegisterActivityCallback */{
    private Button registerButton;
   // private IUserService _userService;
   // private OperationSuccess _registrationSuccess;
   // private RegisterThread _thread;

    private Handler _mainHandler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        setTitle("Register");
        showBack();

        registerButton = findViewById(R.id.sinscrire);
        registerButton.setOnClickListener(this);

       // this._userService = new UserServiceImplementation();
       // this._registrationSuccess = null;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sinscrire:

                TextView firstname =  findViewById(R.id.Firstname);
                TextView lastname = findViewById(R.id.Lastname);
                TextView username =  findViewById(R.id.Username);
                TextView password = findViewById(R.id.Password);

              /*  Person person = new Person();
                person.setFirstname(firstname.getText().toString());
                person.setLastname(lastname.getText().toString());
                person.setUsername(username.getText().toString());
                person.setPassword(password.getText().toString());

                _thread = new RegisterThread(this, person, _userService);
                _thread.start();
                break;*/
            case R.id.DejaClient:
                AuthenticationActivity.displayActivity(com.example.globallive.RegisterActivity.this);
                break;
        }
    }

   /* @Override
    public void callBackSuccess(int userID) {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                HomeActivity.displayActivity(com.example.globallive.RegisterActivity.this);
            }
        });

    }*/

  /*  @Override
    public void callBackFail(String message) {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView _errorDisplay = findViewById(R.id.errorRegister);
                _errorDisplay.setText(message);
            }
        });
    }*/
}
