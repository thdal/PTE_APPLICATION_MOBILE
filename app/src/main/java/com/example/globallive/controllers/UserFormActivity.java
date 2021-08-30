package com.example.globallive.controllers;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.globallive.R;
import com.example.globallive.entities.EventCanal;
import com.example.globallive.entities.EventType;
import com.example.globallive.entities.User;
import com.example.globallive.entities.UserProfil;
import com.example.globallive.services.IUserService;
import com.example.globallive.services.UserServiceImplementation;
import com.example.globallive.tabs.HomeActivity;
import com.example.globallive.threads.DeleteUserThread;
import com.example.globallive.threads.EventUtilsThread;
import com.example.globallive.threads.IDeleteUserCallback;
import com.example.globallive.threads.IPostUserCallback;
import com.example.globallive.threads.IUserUtilsCallback;
import com.example.globallive.threads.PostUserThread;
import com.example.globallive.threads.UserUtilsThread;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserFormActivity extends MainActivity implements IPostUserCallback, IUserUtilsCallback, IDeleteUserCallback {

    private Handler _mainHandler = new Handler();
    private User selectedUser;
    private IUserService _userService;
    public static Context context;
    User currentUser;
    private PostUserThread _postUserThread;
    private DeleteUserThread _deleteUserThread;
    private UserUtilsThread _threadUtils;


    //Nos inputs
    private TextView userFirstName;
    public TextView userLastName;
    private TextView userEmail;
    public Button btnSubmit;
    private Button btnDelete;
    private ImageView imageProfil;
    private Spinner profilSpinner;
    private boolean errorForm;

    //Callback
    private IPostUserCallback cPost;
    private IDeleteUserCallback cDel;
    //Type de form création/update
    private boolean ourProfil = false;
    private boolean createProfil = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_form);
        showBack();
        setTitle("Modifier un utilisateur");
        cPost = this; //callbackPost
        cDel = this; //callbackDel
        //Recup nos objets
        context = this;
        this.currentUser = (User) getIntent().getSerializableExtra("CURRENT_USER");
        selectedUser = (User) getIntent().getSerializableExtra("SELECTED_USER");
        //On vire le logo du header pour gagner en harmonie
        RelativeLayout outer = (RelativeLayout)findViewById(R.id.headerInclude);
        ImageView iv = (ImageView)outer.findViewById(R.id.imageViewLogoGL);
        TextView txtLabel = (TextView)outer.findViewById(R.id.textViewTitle);
        txtLabel.setTypeface(null, Typeface.BOLD);
        iv.setVisibility(View.GONE);
        //Nos inputs
        userFirstName = this.findViewById(R.id.UserFormFirstName);
        userLastName = this.findViewById(R.id.UserFormUserLastName);
        userEmail = this.findViewById(R.id.UserFormUserEmail);
        btnSubmit = this.findViewById(R.id.submitUser);
        btnDelete = this.findViewById(R.id.deleteUser);
        imageProfil = this.findViewById(R.id.imageViewUser);
        profilSpinner = this.findViewById(R.id.spinnerUserProfils);
        //Init nos boutons
        btnSubmit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_edit_24, 0, 0, 0);
        btnSubmit.setText("Modifier");
        btnDelete.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_delete_24, 0, 0, 0);
        btnDelete.setText("Supprimer");
        //Si update
        if(selectedUser.getId() == currentUser.getId()){
            setTitle("Éditer son profil");
            ourProfil = true;
        }
        //Si création
        if(selectedUser.getId() == 0){
            setTitle("Ajouter un utilisateur");
            createProfil = true;
            btnDelete.setVisibility(View.GONE);
            btnSubmit.setText("Ajouter");
        }
        if(!createProfil)
            userFirstName.setText(selectedUser.getFirstName());
            userLastName.setText(selectedUser.getLastName());
            userEmail.setText(selectedUser.getEmail());
        //Init spinner genres
        setUserGender();
        //On init l'image de profile
        setImageViewProfil(createProfil);
        //Recup les types de profiles
        this._userService = new UserServiceImplementation();
        _threadUtils = new UserUtilsThread(this, _userService);
        _threadUtils.start();
        //Boutons
        //Le submit
        Button button = (Button) this.findViewById(R.id.submitUser);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId()) {
                    case R.id.submitUser:
                            getEventFormError();
                            if(errorForm)
                                return;
                            //Si création ou édition d'un utilisateur => objet selectedUser
                            UserProfil selectedProfil = (UserProfil) profilSpinner.getSelectedItem();
                            if (createProfil || !ourProfil) {
                                selectedUser.setFirstName(userFirstName.getText().toString());
                                selectedUser.setLastName(userLastName.getText().toString());
                                selectedUser.setEmail(userEmail.getText().toString());
                                selectedUser.setGenre_id(getGenderId());
                                selectedUser.setProfile_id(selectedProfil.getId());
                                //si createProfil => !update (4eme paramétre)
                                if(createProfil)
                                    selectedUser.setPassword("titre-epsi");
                                _postUserThread = new PostUserThread(cPost, selectedUser, _userService, !createProfil);
                                _postUserThread.start();
                                return;
                            }
                            //Si édition de l'utilisateur connécté => objet currentUser
                            if(ourProfil){
                                currentUser.setFirstName(userFirstName.getText().toString());
                                currentUser.setLastName(userLastName.getText().toString());
                                currentUser.setEmail(userEmail.getText().toString());
                                currentUser.setGenre_id(getGenderId());
                                currentUser.setProfile_id(selectedProfil.getId());
                                _postUserThread = new PostUserThread(cPost, currentUser, _userService, true);
                                _postUserThread.start();
                                return;
                            }
                    break;
                }
            }
        });
        //Le delete
        Button buttonDelete = (Button) this.findViewById(R.id.deleteUser);
        buttonDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId()) {
                    case R.id.deleteUser:
                        _deleteUserThread = new DeleteUserThread(cDel, currentUser.getId() , currentUser.getProfile_id(), _userService);
                        _deleteUserThread.start();
                        break;
                }
            }
        });
    }

    //Callback de notre édition
    @Override
    public void postUserCallbackSuccess(){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                HomeActivity.displayActivity((MainActivity) context, currentUser, "");
            }
        });
    }

    public static Context getTargetContext(){
        return context;
    }

    @Override
    public void getUserProfilsCallback(List<UserProfil> userProfils){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Spinner dropdown = findViewById(R.id.spinnerUserProfils);
                ArrayList<UserProfil> dropdownList = new ArrayList<>();
                for(UserProfil userProfil : userProfils){
                    dropdownList.add(userProfil);
                }
                ArrayAdapter<UserProfil> adapter = new ArrayAdapter<UserProfil>(context, android.R.layout.simple_spinner_dropdown_item, dropdownList);
                dropdown.setAdapter(adapter);
                if(!createProfil)
                    dropdown.setSelection(selectedUser.getProfile_id() -1);
            }
        });
    }

    @Override
    public void deleteUserCallbackSuccess() {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN); //deconnexion
                homeIntent.addCategory( Intent.CATEGORY_HOME );//deconnexion
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//deconnexion
                startActivity(homeIntent);//deconnexion
                MainActivity activity = (MainActivity) context;
                AuthenticationActivity.displayActivity(activity); //Retour connexion
            }
        });
    }

    public void setUserGender(){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                String[] genres = new String[]{"Homme", "Femme"};
                Spinner dropdown = findViewById(R.id.spinnerUserGender);
                ArrayList<String> dropdownList = new ArrayList<>();
                for(String genre : genres){
                    dropdownList.add(genre);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, dropdownList);
                dropdown.setAdapter(adapter);
                if(selectedUser.getGenre_id() == 2)
                    dropdown.setSelection(0);
                else
                    dropdown.setSelection(1);
            }
        });
    }

    //
    public int getGenderId(){
        Spinner mySpinnerGender = (Spinner) this.findViewById(R.id.spinnerUserGender);
        String gender = (String) mySpinnerGender.getSelectedItem();
        //1=Femme 2=Homme
        if(gender == "Femme")
            return 1;
        else
            return 2;
    }

    public void setImageViewProfil(boolean creation){
        if(!creation){
            if(selectedUser.isUserImg()){
                String imgUrl = getString(R.string.api_url) + "/userId"+selectedUser.getId()+"/userImg.jpg";
                Picasso.with(this).cancelRequest(imageProfil);
                Picasso.with(this).load(imgUrl).into(imageProfil);
            }else{
                //Modifier sur le genre
                if(selectedUser.getGenre_id() == 2){
                    Picasso.with(this).load(R.drawable.man).into(imageProfil);
                }else{
                    Picasso.with(this).load(R.drawable.woman).into(imageProfil);
                }
            }
        }else{
            Picasso.with(this).load(R.drawable.man).into(imageProfil);
        }
    }

    private void getEventFormError(){
        if (userFirstName.getText().toString().length() <= 0) {
            userFirstName.setError("Veuillez renseigner un nom svp.");
            errorForm = true;
        }else{
            userFirstName.setError(null);
        }
        if (userLastName.getText().toString().length() <= 0) {
            userLastName.setError("Veuillez renseigner un nom svp.");
            errorForm = true;
        }else{
            userLastName.setError(null);
        }
        if (userEmail.getText().toString().length() <= 0) {
            userEmail.setError("Veuillez renseigner un mail svp.");
            errorForm = true;
        }else{
            userEmail.setError(null);
        }

        if(userFirstName.getError() == null && userLastName.getError() == null
                && userEmail.getError() == null)
            errorForm = false;
    }


}
