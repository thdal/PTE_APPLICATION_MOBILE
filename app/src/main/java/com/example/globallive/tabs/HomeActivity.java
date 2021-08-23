package com.example.globallive.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.globallive.R;
import com.example.globallive.controllers.MainActivity;
import com.example.globallive.entities.EventCanal;
import com.example.globallive.entities.EventType;
import com.example.globallive.entities.User;
import com.example.globallive.services.EventServiceImplementation;
import com.example.globallive.services.IEventService;
import com.example.globallive.threads.EventUtilsThread;
import com.example.globallive.threads.IEventUtilsCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.List;

import androidx.fragment.app.FragmentPagerAdapter;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivity extends MainActivity implements NavigationView.OnNavigationItemSelectedListener, IEventUtilsCallback {


    //ADD
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    ViewPager pager;
    TabLayout mTabLayout;
    TabItem firstItem,secondItem;
    PagerAdapter adapter;
    private int userID;
    private User currentUser;
    private IEventService _eventService;
    private EventUtilsThread _thread;
    private List<EventType> _eventTypes;
    private List<EventCanal> _eventCanaux;

    private Handler _mainHandler = new Handler();

    public static void displayActivity(MainActivity activity, User currentUser, String msg){
        Intent intent = new Intent(activity,HomeActivity.class);
        intent.putExtra("CURRENT_USER", (Serializable) currentUser);
        intent.putExtra("CURRENT_USER_ID", currentUser.getId());
        intent.putExtra("MESSAGE", msg);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.userID = getIntent().getIntExtra("CURRENT_USER_ID", 0);
        this.currentUser = (User) getIntent().getSerializableExtra("CURRENT_USER");
        //On récupére nos listes de canaux et de catégories
        this._eventService = new EventServiceImplementation();
        _thread = new EventUtilsThread(this, _eventService);
        _thread.start();
        //Toute cette partie jusqu'à la fin de la fonction sert à manipuler nos fragments (Navbar)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tablayout);
        firstItem = findViewById(R.id.firstItem);
        secondItem = findViewById(R.id.secondItem);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        //On met personnalise notre barre de navigation avec nom/prenom/mail
        //Reste Image ect.
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.textViewNavHeaderName);
        TextView navMail = (TextView) headerView.findViewById(R.id.textViewNavHeaderMail);
        navUsername.setText(currentUser.getFirstName());
        navMail.setText(currentUser.getEmail());
        //Image Genre 1 = Femme , 2 = Homme
        //Si l'utilisateur a une image perso
        CircleImageView circleImageProfile = headerView.findViewById(R.id.imageViewEvent);
        if(currentUser.isUserImg()){
            String imgUrl = getString(R.string.api_url) + "/userId"+currentUser.getId()+"/userImg.jpg";
            Picasso.get().load(imgUrl).into(circleImageProfile);
        }else{
            if(currentUser.getGenre_id() == 2){
                circleImageProfile.setImageResource(R.drawable.man);
            }else{
                circleImageProfile.setImageResource(R.drawable.woman);
            }
        }
        //Affiche les icones du panneau latéral gauche en couleur
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        adapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,mTabLayout.getTabCount(), this.currentUser);
        pager.setAdapter(adapter);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 1){
                    setTitle("Ajouter un événement");
                }
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
    }

    //On a cliqué sur un item du menu latéral gauche (Catégories/canaux)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        int sortBy = 0;
        if(item.getGroupId() == R.id.grpCategorie || item.getGroupId() == R.id.grpCanaux){
            if(item.getGroupId() == R.id.grpCategorie)
            {
                sortBy = 1;
            }
            else{
                sortBy = 2;
            }
        }
        if(item.getGroupId() == R.id.grpFirst){
            if(item.getItemId() == R.id.menuTabGetlAll){
                sortBy = 3;
            }else{
                sortBy = 4;
            }
        }
            //Recup le fragment actuel
            Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
            //On call la méthode d'update de la vue
            ((EventListFragment)page).updateEvents(sortBy, item.getItemId());
            pager.setCurrentItem(0);

        //Toast.makeText(this, "Btn is clicked.", Toast.LENGTH_SHORT).show();
        return false;
    }

    //Item selectionné du menu triple dot
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.rightMenuItemSignOut:
                Intent homeIntent = new Intent(Intent.ACTION_MAIN); //deconnexion
                homeIntent.addCategory( Intent.CATEGORY_HOME );//deconnexion
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//deconnexion
                startActivity(homeIntent);//deconnexion
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //On récupére la liste des catégories de notre bdd et on init le menu latéral gauche avec les icons et les labels
    @Override
    public void getEventTypesCallback(List<EventType> eventTypes){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                _eventTypes = eventTypes;
                Menu menu = navigationView.getMenu();
                MenuItem itemFirstLabel = menu.add(R.id.grpCategorie, Menu.NONE, Menu.NONE, "Catégories");
                itemFirstLabel.setEnabled(false);
                for(EventType eventType : _eventTypes) {
                   MenuItem item = menu.add(R.id.grpCategorie, eventType.getId(), Menu.NONE, eventType.getTypeEventName());
                    String mDrawableName = stripAccents(eventType.getTypeEventName().toLowerCase());
                    //On récupére et on assigne nos icons dynamiquement
                    int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
                   item.setIcon(resID);
                }
            }
        });
    }

    //On récupére la liste des canaux de notre bdd et on init le menu latéral gauche avec les icons et les labels
    @Override
    public void getEventCanauxCallback(List<EventCanal> eventCanaux){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                _eventCanaux = eventCanaux;
                Menu menu = navigationView.getMenu();
                MenuItem itemFirstLabel = menu.add(R.id.grpCanaux, Menu.NONE, Menu.NONE, "Canaux");
                itemFirstLabel.setEnabled(false);
                for(EventCanal eventCanal : _eventCanaux) {
                    MenuItem item = menu.add(R.id.grpCanaux, eventCanal.getId(), Menu.NONE, eventCanal.getCanalEventName());
                    String mDrawableName = stripAccents(eventCanal.getCanalEventName().toLowerCase());
                    //On récupére et on assigne nos icons dynamiquement
                    int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
                    item.setIcon(resID);
                }
            }
        });
    }

    //On vire les accents de notre string pour matcher avec nos images en local
    public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    //Menu de droite tripledot (met à jour la vue)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_menu, menu);
        return true;
    }


}




