package com.example.globallive.tabs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.globallive.R;
import com.example.globallive.controllers.AuthenticationActivity;
import com.example.globallive.controllers.MainActivity;
import com.example.globallive.controllers.UserFormActivity;
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
import androidx.appcompat.widget.SearchView;
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
    TabItem firstItem,secondItem,thirdItem;
    //TabItem firstItem,secondItem;
    PagerAdapter adapter;
    private int userID;
    private User currentUser;
    private IEventService _eventService;
    //Servira dans le menu latéral gauche (recup label/icons catégories/canaux)
    private EventUtilsThread _thread;
    private List<EventType> _eventTypes;
    private List<EventCanal> _eventCanaux;
    private Context context;
    private String TITRE_HOME = "Liste d'événements";
    private String TITRE_FORM = "Ajouter un événement";
    private String TITRE_ADMIN = "Liste des utilisateurs";

    private Handler _mainHandler = new Handler();
    private boolean homeTab = true;
    private Menu menu;

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
        this.context = this;
        this.userID = getIntent().getIntExtra("CURRENT_USER_ID", 0);
        this.currentUser = (User) getIntent().getSerializableExtra("CURRENT_USER");
        //On récupére nos listes de canaux et de catégories
        this._eventService = new EventServiceImplementation();
        _thread = new EventUtilsThread(this, _eventService);
        _thread.start();
        //Toute cette partie jusqu'à la fin de la fonction sert à manipuler nos fragments (Navbar)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(TITRE_HOME);
        pager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tablayout);
        firstItem = findViewById(R.id.firstItem);
        secondItem = findViewById(R.id.secondItem);
        //thirdItem = findViewById(R.id.thirdItem);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        //On personnalise notre barre de navigation avec nom/prenom/mail
        //Reste Image ect.
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.textViewNavHeaderName);
        TextView navMail = (TextView) headerView.findViewById(R.id.textViewNavHeaderMail);
        navUsername.setText(currentUser.getFirstName() + ' ' + currentUser.getLastName());
        navMail.setText(currentUser.getEmail());
        //Image Genre 1 = Femme , 2 = Homme
        //Si l'utilisateur a une image perso
        CircleImageView circleImageProfile = headerView.findViewById(R.id.imageViewEvent);
        if(currentUser.isUserImg()){
            String imgUrl = getString(R.string.api_url) + "/userId"+currentUser.getId()+"/userImg.jpg";
            Picasso.with(this).load(imgUrl).into(circleImageProfile);

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
                if(tab.getPosition() == 0){
                    homeTab = true;
                    getSupportActionBar().setTitle(TITRE_HOME);
                    Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
                    if(page != null)
                        ((EventListFragment)page).updateEvents(0, 0);
                }else{
                    homeTab = false;
                }
                if(tab.getPosition() == 1){
                    getSupportActionBar().setTitle(TITRE_FORM);
                    setTitle("Ajouter un événement");
                }
                if(tab.getPosition() == 2){
                    getSupportActionBar().setTitle(TITRE_ADMIN);
                    Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 2);
                    if(page != null)
                        ((UserListFragment)page).updateUsers();
                }
                pager.setCurrentItem(tab.getPosition());
                hideResearch();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Si on est visiteur -> pas de création d'event on vire un tab de navigation
        if(currentUser.getProfile_id() == 0){
            ((ViewGroup) mTabLayout.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
        }
        if(currentUser.getProfile_id() != 3){
            ((ViewGroup) mTabLayout.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        }
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    //On a cliqué sur un item du menu latéral gauche (Catégories/canaux)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        getSupportActionBar().setSubtitle(item.getTitle());

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
        if(item.getGroupId() == R.id.grpReglementation){
            Log.d("itemid", String.valueOf(item.getItemId()));
            String strCguId = "menuTabCGU";
            String strRgpdId = "menuTabRGPD";
            String strMlId = "menuTabMentions";
            String url ="";
            int resourceId1 = getResources().getIdentifier("resource_"+strCguId, "string", getPackageName());
            int resourceId2 = getResources().getIdentifier("resource_"+strRgpdId, "string", getPackageName());
            int resourceId3 = getResources().getIdentifier("resource_"+strMlId, "string", getPackageName());

            if(item.getItemId() == 111) {
                url = "https://pte-epsi.thibaut-dalens.com/#/cgu";
                Log.d("itemid1", String.valueOf(resourceId1));

            }
            if(item.getItemId() == 222) {
                url = "https://pte-epsi.thibaut-dalens.com/#/privacyPolicy";
                Log.d("itemid2", String.valueOf(resourceId2));

            }
            if(item.getItemId() == 333) {
                url = "https://pte-epsi.thibaut-dalens.com/#/legalNotices";
                Log.d("itemid3", String.valueOf(resourceId3));

            }
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                startActivity(viewIntent);
                return false;
        }
            //Recup le fragment actuel
            Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
            //On call la méthode d'update de la vue
            ((EventListFragment)page).updateEvents(sortBy, item.getItemId());
            pager.setCurrentItem(0);//0=liste des événements

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
                AuthenticationActivity.displayActivity(this); //Retour connexion
                return true;
            case R.id.rightMenuEditProfil:
                Intent intent = new Intent(this, UserFormActivity.class);
                intent.putExtra("SELECTED_USER", (Serializable) this.currentUser );
                intent.putExtra("CURRENT_USER", (Serializable) this.currentUser );
                startActivity(intent);

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
                //Set menu items CGU RGPD MENTIONS LEGALES
                setLawMenu();


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
        inflater.inflate(R.menu.search_menu, menu);
        this.menu = menu;
        MenuItem item = menu.findItem(R.id.action_search);
        if (homeTab) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }

        SearchView searchView = (SearchView) item.getActionView();

        //Action du searchView
        searchView.setQueryHint("Rechercher");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText){
                Log.d("jetapeici", "ici");
                Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
                if(page != null)
                    ((EventListFragment)page).searchEvents(newText);
                return false;
            }
        });
        return true;
    }

    public void hideResearch(){
        MenuItem item = menu.findItem(R.id.action_search);
        if (homeTab) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
    }

    private void setLawMenu(){
        Menu menu = navigationView.getMenu();
        MenuItem itemFirstLabel = menu.add(R.id.grpReglementation, Menu.NONE, Menu.NONE, "Articles de loi");
        itemFirstLabel.setEnabled(false);
        //CGU
        MenuItem itemloi = menu.add(R.id.grpReglementation,111, Menu.NONE, "Conditions générales d\'utilisation");
        int resID = getResources().getIdentifier("cgu" , "drawable", getPackageName());
        //itemloi.setIcon(resID);
        // RGPD
        MenuItem itemloiRgpd = menu.add(R.id.grpReglementation,222, Menu.NONE, "Politique de confidentialité");
        int resIDRgpd = getResources().getIdentifier("rgpd" , "drawable", getPackageName());
        //itemloiRgpd.setIcon(resIDRgpd);
        // Mentions légales
        MenuItem itemloiMl = menu.add(R.id.grpReglementation,333, Menu.NONE, "Mentions légales");
        int resIDMl = getResources().getIdentifier("mentions" , "drawable", getPackageName());
        //itemloiMl.setIcon(resIDMl);
    }



}




