package com.example.globallive.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.globallive.R;
import com.example.globallive.controllers.MainActivity;
import com.example.globallive.entities.Event;
import com.example.globallive.entities.EventAdapter;
import com.example.globallive.services.EventServiceImplementation;
import com.example.globallive.services.IEventService;
import com.example.globallive.threads.HomeThread;
import com.example.globallive.threads.IHomeActivityResult;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentPagerAdapter;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    //ADD
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    ViewPager pager;
    TabLayout mTabLayout;
    TabItem firstItem,secondItem,thirdItem,fourthItem;
    PagerAdapter adapter;

    private Handler _mainHandler = new Handler();

    public static void displayActivity(MainActivity activity, int currentUser, String msg){
        Intent intent = new Intent(activity,HomeActivity.class);
        intent.putExtra("CURRENT_USER_ID", currentUser);
        intent.putExtra("MESSAGE", msg);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Toute cette partie jusqu'à la fin de la fonction sert à manipuler nos fragments
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tablayout);

        firstItem = findViewById(R.id.firstItem);
        secondItem = findViewById(R.id.secondItem);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        adapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,mTabLayout.getTabCount());
        pager.setAdapter(adapter);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));


        //findViewById(R.id.buttonGroupes).setOnClickListener(this);
       // findViewById(R.id.buttonCategorie).setOnClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(item.getItemId() == R.id.menuTab){
            Toast.makeText(this, "Btn is clicked.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}




