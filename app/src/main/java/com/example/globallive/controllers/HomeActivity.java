package com.example.globallive.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.globallive.R;

public class HomeActivity extends MainActivity implements View.OnClickListener {

    public static void displayActivity(MainActivity activity){
        Intent intent = new Intent(activity,HomeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Acceuil");

        //findViewById(R.id.buttonGroupes).setOnClickListener(this);
       // findViewById(R.id.buttonCategorie).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
           /* case R.id.buttonGroupes:
                GroupActivity.displayActivity(this);
                break;
            case R.id.buttonCategorie:
                CategorieActivity.displayActivity(this);
                break;*/
        }
    }
}




