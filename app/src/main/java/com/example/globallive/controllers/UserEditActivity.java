package com.example.globallive.controllers;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.globallive.R;
import com.example.globallive.entities.User;
import com.example.globallive.services.IUserService;
import com.example.globallive.services.UserServiceImplementation;
import com.example.globallive.tabs.HomeActivity;
import com.example.globallive.threads.IPostUserCallback;
import com.example.globallive.threads.PostUserThread;

public class UserEditActivity extends MainActivity implements IPostUserCallback {

    private Handler _mainHandler = new Handler();
    private User selectedUser;
    private IUserService _userService;
    Context context = this;
    User currentUser;
    private PostUserThread _postUserThread;

    //Nos inputs
    private TextView userFirstName;
    private TextView userLastName;
    private TextView userEmail;
    private Button btnSubmit;
    //Callback
    private IPostUserCallback c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_form);
        showBack();
        setTitle("Modifier un utilisateur");
        c = this;
        this.currentUser = (User) getIntent().getSerializableExtra("CURRENT_USER");
        //L'événement
        selectedUser = (User) getIntent().getSerializableExtra("SELECTED_USER");
        //On vire le logo du header pour gagner en harmonie
        RelativeLayout outer = (RelativeLayout)findViewById(R.id.headerInclude);
        ImageView iv = (ImageView)outer.findViewById(R.id.imageViewLogoGL);
        TextView txtLabel = (TextView)outer.findViewById(R.id.textViewTitle);
        txtLabel.setTypeface(null, Typeface.BOLD);
        iv.setVisibility(View.GONE);
        this._userService = new UserServiceImplementation();
        userFirstName = this.findViewById(R.id.UserFormFirstName);
        userLastName = this.findViewById(R.id.UserFormUserLastName);
        userEmail = this.findViewById(R.id.UserFormUserEmail);
        btnSubmit = this.findViewById(R.id.submitUser);
        //addIcon btnSubmit
        btnSubmit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_edit_24, 0, 0, 0);
        btnSubmit.setText("Modifier");
        userFirstName.setText(selectedUser.getFirstName());
        userLastName.setText(selectedUser.getLastName());
        userEmail.setText(selectedUser.getEmail());
        //Le submit
        Button button = (Button) this.findViewById(R.id.submitUser);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId()) {
                    case R.id.submitUser:
                            //Si création new User sinon selected
                            selectedUser.setFirstName(userFirstName.getText().toString());
                            selectedUser.setFirstName(userFirstName.getText().toString());
                            selectedUser.setFirstName(userFirstName.getText().toString());
                            _postUserThread = new PostUserThread(c, selectedUser, _userService, true);
                            _postUserThread.start();
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
}
