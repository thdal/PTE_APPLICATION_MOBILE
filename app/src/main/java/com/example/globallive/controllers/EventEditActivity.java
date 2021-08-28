package com.example.globallive.controllers;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.globallive.R;
import com.example.globallive.entities.Event;
import com.example.globallive.entities.EventCanal;
import com.example.globallive.entities.EventType;
import com.example.globallive.entities.User;
import com.example.globallive.services.EventServiceImplementation;
import com.example.globallive.services.IEventService;
import com.example.globallive.tabs.HomeActivity;
import com.example.globallive.threads.EventUtilsThread;
import com.example.globallive.threads.IEventUtilsCallback;
import com.example.globallive.threads.IPostEventCallback;
import com.example.globallive.threads.PostEventThread;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventEditActivity extends MainActivity implements IEventUtilsCallback, IPostEventCallback {

    private Handler _mainHandler = new Handler();
    private Event selectedEvent;
    private IEventService _eventService;
    private EventUtilsThread _thread;
    Context context = this;
    User currentUser;
    private PostEventThread _postEventThread;
    //DatePicker
    private EditText editTextDate;
    private Button buttonDate;
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;
    //HourPicker
    private EditText editTextHour;
    private int lastSelectedHour = -1;
    private int lastSelectedMinute = -1;
    //Nos inputs
    TextView _errorDisplay;
    private TextView eventTitle ;
    private TextView eventLink;
    private TextView eventDate;
    private TextView eventHour;
    private String eventHourstr;
    private TextView eventAddress;
    private TextView eventDescription;
    private Button btnSubmit;
    private Spinner mySpinnerCat;
    private Spinner mySpinnerCan;
    //Callback
    private IPostEventCallback c;
    //
    Boolean errorForm = true;


    public static void displayActivity(MainActivity activity, User user, Event event){
        Intent intent = new Intent(activity, EventEditActivity.class);
        intent.putExtra("SELECTED_EVENT", event );
        intent.putExtra("CURRENT_USER",  user );
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event_form);
        showBack();
        setTitle("Modifier un événement");
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Modifier un événement");
        c = this;
        this.currentUser = (User) getIntent().getSerializableExtra("CURRENT_USER");
        //L'événement
        selectedEvent = (Event) getIntent().getSerializableExtra("SELECTED_EVENT");
        //On vire le logo du header pour gagner en harmonie
        RelativeLayout outer = (RelativeLayout)findViewById(R.id.headerInclude);
        ImageView iv = (ImageView)outer.findViewById(R.id.imageViewLogoGL);
        TextView txtLabel = (TextView)outer.findViewById(R.id.textViewTitle);
        txtLabel.setTypeface(null, Typeface.BOLD);
        iv.setVisibility(View.GONE);
        //On vire tout
        //outer.setVisibility(View.GONE);
        //On init notre service et on l'envoie au thread cela permettra d'afficher
        //dynamiquement les catégories et les canaux, le thread permet un appel non bloquant.
        this._eventService = new EventServiceImplementation();
        _thread = new EventUtilsThread(this, _eventService);
        _thread.start();
        _errorDisplay = this.findViewById(R.id.errorEventForm);
        eventTitle = this.findViewById(R.id.EventFormEventTitle);
        eventLink = this.findViewById(R.id.EventFormEventLink);
        eventDate = this.findViewById(R.id.editText_date);
        eventHour = this.findViewById(R.id.editText_hour);
        eventAddress = this.findViewById(R.id.EventFormEventAddress);
        eventDescription = this.findViewById(R.id.EventFormEventDescription);
        btnSubmit = this.findViewById(R.id.submitEvent);
        //addIcon btnSubmit
        btnSubmit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_edit_24, 0, 0, 0);
        mySpinnerCat = (Spinner) this.findViewById(R.id.spinnerEventFormCat);
        mySpinnerCan = (Spinner) this.findViewById(R.id.spinnerEventFormCanal);
        btnSubmit.setText("Modifier");
        eventTitle.setText(selectedEvent.getEventName());
        eventLink.setText(selectedEvent.getEventLink());
        eventAddress.setText(selectedEvent.getEventAddress());
        eventDescription.setText(selectedEvent.getEventDescription());
        if(selectedEvent.getEventHour() == null){
            this.eventHourstr = "--:--";
        }else{
            this.eventHourstr = selectedEvent.getEventHour().toString();
        }
        eventHour.setText(eventHourstr);
        //Date to string
        Date date = selectedEvent.getEventDate();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(date);
        //string to the input
        eventDate.setText(strDate);
        //Btn date
        //Select date
        this.editTextDate = (EditText) this.findViewById(R.id.editText_date);
        EditText textDate = this.findViewById(R.id.editText_date);
        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DO STUFF
                buttonSelectDate();
            }
        });
        //SelectHour
        this.editTextHour = (EditText) this.findViewById(R.id.editText_hour);
        editTextHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DO STUFF
                buttonSelectHour();
            }
        });
        //Le submit
        Button button = (Button) this.findViewById(R.id.submitEvent);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId()) {
                    case R.id.submitEvent:
                        getEventFormError();
                        if(errorForm)
                            _errorDisplay.setText("Veuillez remplir tous les champs svp");
                        //Sinon on post
                        else
                            try {
                                EventType typeSelected = (EventType) mySpinnerCat.getSelectedItem();
                                EventCanal canalSelected = (EventCanal) mySpinnerCan.getSelectedItem();
                                Event eventToSend = new Event();
                                eventToSend.setTypeEventId(typeSelected.getId());
                                eventToSend.setCanalEventId(canalSelected.getId());
                                eventToSend.setEventName(eventTitle.getText().toString());
                                eventToSend.setEventImg(false); // pas d'upload d'image pr linstant sur lapp mobile
                                eventToSend.setEventAddress(eventAddress.getText().toString());
                                eventToSend.setEventDescription(eventDescription.getText().toString());
                                eventToSend.setEventLink(eventLink.getText().toString());
                                //String to hour
                                String time = eventHour.getText().toString();
                                eventToSend.setEventHour(time);
                                //On parse la string au format date
                                Date date = null;
                                date = new SimpleDateFormat("dd/MM/yyyy").parse(eventDate.getText().toString());
                                //le modele se charge d'un deuxieme parse au format de la bdd yyyy-MM-dd voir annotation dans le fichier
                                eventToSend.setEventDate(date);
                                eventToSend.setId(selectedEvent.getId());
                                eventToSend.setUserId(selectedEvent.getUserId());
                                Toast.makeText(context,  String.valueOf(selectedEvent.getId()), Toast.LENGTH_SHORT).show();
                                _postEventThread = new PostEventThread(c, eventToSend, _eventService, true);
                                _postEventThread.start();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        break;
                }
            }
        });

    }

    @Override
    public void getEventTypesCallback(List<EventType> eventTypes){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //get the spinner from the xml.
                Spinner dropdown = findViewById(R.id.spinnerEventFormCat);
                //create a list of items for the spinner.
                //Nos objets models avec méthode toString() réécrite
                ArrayList<EventType> dropdownList = new ArrayList<>();
                for(EventType eventType : eventTypes){
                    dropdownList.add(eventType);
                }
                //create an adapter to describe how the items are displayed, adapters are used in several places in android.
                //There are multiple variations of this, but this is the basic variant.
                ArrayAdapter<EventType> adapter = new ArrayAdapter<EventType>(context, android.R.layout.simple_spinner_dropdown_item, dropdownList);
                //set the spinners adapter to the previously created one.
                dropdown.setAdapter(adapter);
                dropdown.setSelection(selectedEvent.getTypeEventId() -1);
            }
        });
    }

    @Override
    public void getEventCanauxCallback(List<EventCanal> eventCanaux){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Spinner dropdown = findViewById(R.id.spinnerEventFormCanal);
                ArrayList<EventCanal> dropdownList = new ArrayList<>();
                for(EventCanal eventCanal : eventCanaux){
                    dropdownList.add(eventCanal);
                }
                ArrayAdapter<EventCanal> adapter = new ArrayAdapter<EventCanal>(context, android.R.layout.simple_spinner_dropdown_item, dropdownList);
                dropdown.setAdapter(adapter);
                dropdown.setSelection(selectedEvent.getCanalEventId() -1);
            }
        });
    }

    // User click on 'Select Date' button.
    private void buttonSelectDate() {
        // Date Select Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editTextDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                lastSelectedYear = year;
                lastSelectedMonth = monthOfYear;
                lastSelectedDayOfMonth = dayOfMonth;
            }
        };
        DatePickerDialog datePickerDialog = null;
        datePickerDialog = new DatePickerDialog(context, dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
        // Show
        datePickerDialog.show();
    }

    // User click on 'Select Hour' button.
    private void buttonSelectHour() {
        if(this.lastSelectedHour == -1)  {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            this.lastSelectedHour = c.get(Calendar.HOUR_OF_DAY);
            this.lastSelectedMinute = c.get(Calendar.MINUTE);
        }
        // Time Set Listener.
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editTextHour.setText(hourOfDay + ":" + minute );
                lastSelectedHour = hourOfDay;
                lastSelectedMinute = minute;
            }
        };
        TimePickerDialog timePickerDialog = null;
        timePickerDialog = new TimePickerDialog(this, timeSetListener, lastSelectedHour, lastSelectedMinute, true);
        // Show
        timePickerDialog.show();
    }

    //Callback de notre édition
    @Override
    public void postEventCallbackSuccess(){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                HomeActivity.displayActivity((MainActivity) context, currentUser, "");
            }
        });
    }

    //Menu de droite tripledot (met à jour la vue)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_menu, menu);
        return true;
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
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getEventFormError(){
        if (eventTitle.getText().toString().length() <= 0) {
            eventTitle.setError("Veuillez renseigner un titre svp.");
            errorForm = true;
        }else{
            eventTitle.setError(null);
        }
        if (eventLink.getText().toString().length() <= 0) {
            eventLink.setError("Veuillez renseigner un lien svp.");
            errorForm = true;
        }else{
            eventLink.setError(null);
        }
        if (eventAddress.getText().toString().length() <= 0) {
            eventAddress.setError("Veuillez renseigner une adresse svp.");
            errorForm = true;
        }else{
            eventAddress.setError(null);
        }
        if (eventDate.getText().toString().length() <= 0) {
            eventDate.setError("Veuillez renseigner une date svp.");
            errorForm = true;
        }else{
            eventDate.setError(null);
        }
        if (eventDescription.getText().toString().length() <= 0) {
            eventDescription.setError("Veuillez renseigner une description svp.");
            errorForm = true;
        }else{
            eventDescription.setError(null);
        }

        if(eventTitle.getError() == null && eventLink.getError() == null
                && eventAddress.getError() == null && eventDate.getError() == null && eventDescription.getError() == null)
            errorForm = false;
    }
}
