package com.example.globallive.controllers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    //Inputs
    TextView firstname;
    TextView lastname;
    TextView email;
    TextView password;
    //Form
    Boolean errorForm = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        setTitle("Inscription");
        showBack();
        //Init inputs
        firstname =  findViewById(R.id.Firstname);
        lastname = findViewById(R.id.Lastname);
        email =  findViewById(R.id.Email);
        password = findViewById(R.id.Password);

        registerButton = findViewById(R.id.sinscrire);
        registerButton.setOnClickListener(this);

        this._userService = new UserServiceImplementation();
        this._registrationSuccess = null;

        //Liens clickables vers nos CGU et RGPD depuis la checkbox
        SpannableString linkCGU = makeLinkSpan("Conditions générales d'utilisation", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://pte-epsi.thibaut-dalens.com/#/cgu";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        SpannableString linkRGPD = makeLinkSpan("Politique de confidentialité", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://pte-epsi.thibaut-dalens.com/#/privacyPolicy";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        // Met à jour le text de la checkbox avec nos liens vers CGU RGPD
        CheckBox checkBoxRGPD = findViewById(R.id.rgpdCheckBox);
        checkBoxRGPD.setText("En vous inscrivant, vous acceptez nos ");
        checkBoxRGPD.append(linkCGU);
        checkBoxRGPD.append(".  Découvrez comment nous recueillons, utilisons et partageons vos données en lisant notre ");
        checkBoxRGPD.append(linkRGPD);
        checkBoxRGPD.append(". (RGPD)");
        makeLinksFocusable(checkBoxRGPD);
        checkBoxRGPD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if ( isChecked ){
                    checkBoxRGPD.setError(null);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sinscrire:

                CheckBox checkBoxRGPD = findViewById(R.id.rgpdCheckBox);
                if(!checkBoxRGPD.isChecked()){
                    checkBoxRGPD.setError("Vous devez accepter les conditions générales d'utilisation ainsi que notre politique de confidentialité pour pouvoir vous enregistrer.");
                    checkBoxRGPD.requestFocus();
                    errorForm = true;
                }

                if (firstname.getText().toString().length() <= 0) {
                    firstname.setError("Veuillez renseignez un prénom svp.");
                    errorForm = true;
                }else{
                    firstname.setError(null);
                }
                if (email.getText().toString().length() <= 0) {
                    email.setError("Veuillez renseignez un email svp.");
                    errorForm = true;
                }else{
                    email.setError(null);
                }
                if (lastname.getText().toString().length() <= 0) {
                    lastname.setError("Veuillez renseignez un nom svp.");
                    errorForm = true;
                }else{
                    lastname.setError(null);
                }
                if (password.getText().toString().length() <= 0) {
                    password.setError("Veuillez renseignez un password svp.");
                    errorForm = true;
                }else{
                    password.setError(null);
                }

                if(checkBoxRGPD.isChecked() && firstname.getError() == null && email.getError() == null && lastname.getError() == null
                && password.getError() == null)
                    errorForm = false;

                if(errorForm){
                    return;
                }else{
                    User user = new User();
                    user.setFirstName(firstname.getText().toString());
                    user.setLastName(lastname.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPassword(password.getText().toString());
                    user.setGenre_id(2);//Homme
                    user.setProfile_id(1);//Organisateur
                    user.setUserImg(false); // false pas d'image
                    user.setIsBanned(false); //false

                    _thread = new RegisterThread(this, user, _userService);
                    _thread.start();
                }

                break;
            case R.id.DejaClient:
                AuthenticationActivity.displayActivity(RegisterActivity.this);
                break;
        }
    }

    @Override
    public void callBackSuccess(User user) {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                HomeActivity.displayActivity(RegisterActivity.this, user, "");
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

    /*
    / onclick span
     */

    private SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
        SpannableString link = new SpannableString(text);
        link.setSpan(new ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return link;
    }

    private void makeLinksFocusable(TextView tv) {
        MovementMethod m = tv.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (tv.getLinksClickable()) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    /*
     * ClickableString class
     */

    private static class ClickableString extends ClickableSpan {
        private View.OnClickListener mListener;
        public ClickableString(View.OnClickListener listener) {
            mListener = listener;
        }
        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
}
