package com.example.globallive.tabs;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.globallive.R;
import com.example.globallive.controllers.MainActivity;
import com.example.globallive.entities.Event;
import com.example.globallive.entities.EventCanal;
import com.example.globallive.entities.EventType;
import com.example.globallive.entities.User;
import com.example.globallive.services.EventServiceImplementation;
import com.example.globallive.services.IEventService;
import com.example.globallive.threads.EventUtilsThread;
import com.example.globallive.threads.IEventUtilsCallback;
import com.example.globallive.threads.IPostEventCallback;
import com.example.globallive.threads.PostEventThread;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventFormFragment extends Fragment  implements IEventUtilsCallback, IPostEventCallback {

    private EventUtilsThread _thread;
    private PostEventThread _postEventThread;

    private IEventService _eventService;
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
    //Inputs
    TextView _errorDisplay;;
    TextView eventTitle;
    TextView eventLink;
    TextView eventDate;
    TextView eventHour;
    TextView eventAddress;
    TextView eventDescription;
    //
    Boolean errorForm = true;



    private User user;
    private int userID;
    private IPostEventCallback c;

    private Handler _mainHandler = new Handler();
    //Constructor obligatoire dans un fragment
    public EventFormFragment(User user) {
        //On init notre service et on l'envoie au thread cela permettra d'afficher
        //dynamiquement les catégories et les canaux, le thread permet un appel non bloquant.
        this.user = user;
        this.userID = user.getId();
        this._eventService = new EventServiceImplementation();
        _thread = new EventUtilsThread(this, _eventService);
        _thread.start();
        c = this;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_form, container, false);
        //On traite sur notre btn pr laffichage dynamique
        Button buttonSubmit = (Button) view.findViewById(R.id.submitEvent);
        buttonSubmit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_add_24, 0, 0, 0);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (70 * scale + 0.5f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                pixels
        );
        params.setMargins(0, 0, 0, 200);
        buttonSubmit.setLayoutParams(params);
        //On vire le logo du header pour gagner en harmonie
        RelativeLayout outer = (RelativeLayout)view.findViewById(R.id.headerInclude);
        ImageView iv = (ImageView)outer.findViewById(R.id.imageViewLogoGL);
        TextView txtLabel = (TextView)outer.findViewById(R.id.textViewTitle);
        txtLabel.setTypeface(null, Typeface.BOLD);
        iv.setVisibility(View.GONE);
        //On vire tout
        outer.setVisibility(View.GONE);
        //Instance inputs
        _errorDisplay = view.findViewById(R.id.errorEventForm);
        eventTitle = view.findViewById(R.id.EventFormEventTitle);
        eventLink = view.findViewById(R.id.EventFormEventLink);
        eventDate = view.findViewById(R.id.editText_date);
        eventHour = view.findViewById(R.id.editText_hour);
        eventAddress = view.findViewById(R.id.EventFormEventAddress);
        eventDescription = view.findViewById(R.id.EventFormEventDescription);

        buttonSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Spinner mySpinnerCat = (Spinner) getActivity().findViewById(R.id.spinnerEventFormCat);
                EventType typeSelected = (EventType) mySpinnerCat.getSelectedItem();
                Spinner mySpinnerCan = (Spinner) getActivity().findViewById(R.id.spinnerEventFormCanal);
                EventCanal canalSelected = (EventCanal) mySpinnerCan.getSelectedItem();

                //Si un des champs n'est pas rempli
                getEventFormError();

                if(errorForm)
                    _errorDisplay.setText("Veuillez remplir tous les champs");
                //Sinon on post
                else{
                    try {
                        _errorDisplay.setText("");
                        Event eventToSend = new Event();
                        eventToSend.setTypeEventId(typeSelected.getId());
                        eventToSend.setCanalEventId(canalSelected.getId());
                        eventToSend.setEventName(eventTitle.getText().toString());
                        eventToSend.setEventImg(false); // pas d'upload d'image pr linstant sur lapp mobile
                        eventToSend.setEventAddress(eventAddress.getText().toString());
                        eventToSend.setEventDescription(eventDescription.getText().toString());
                        eventToSend.setEventLink(eventLink.getText().toString());
                        eventToSend.setEventHour(eventHour.getText().toString());
                        //On parse la string au format date
                        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(eventDate.getText().toString());
                        //le modele se charge d'un deuxieme parse au format de la bdd yyyy-MM-dd voir annotation dans le fichier
                        eventToSend.setEventDate(date);
                        eventToSend.setUserId(userID);
                        _postEventThread = new PostEventThread(c, eventToSend, _eventService, false);
                        _postEventThread.start();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //Select date
        this.editTextDate = (EditText) view.findViewById(R.id.editText_date);
        EditText textDate = view.findViewById(R.id.editText_date);
        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DO STUFF
                buttonSelectDate();
            }
        });
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH);
        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        //SelectHour
        this.editTextHour = (EditText) view.findViewById(R.id.editText_hour);
        editTextHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DO STUFF
                buttonSelectHour();
            }
        });
        return view;
    }

    @Override
    public void getEventTypesCallback(List<EventType> eventTypes){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //get the spinner from the xml.
                Spinner dropdown = getActivity().findViewById(R.id.spinnerEventFormCat);
                //create a list of items for the spinner.
                //Nos objets models avec méthode toString() réécrite
                ArrayList<EventType> dropdownList = new ArrayList<>();
                for(EventType eventType : eventTypes){
                    dropdownList.add(eventType);
                }
                //create an adapter to describe how the items are displayed, adapters are used in several places in android.
                //There are multiple variations of this, but this is the basic variant.
                ArrayAdapter<EventType> adapter = new ArrayAdapter<EventType>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dropdownList);
                //set the spinners adapter to the previously created one.
                dropdown.setAdapter(adapter);
            }
        });
    }

    @Override
    public void getEventCanauxCallback(List<EventCanal> eventCanaux){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Spinner dropdown = getActivity().findViewById(R.id.spinnerEventFormCanal);
                ArrayList<EventCanal> dropdownList = new ArrayList<>();
                for(EventCanal eventCanal : eventCanaux){
                    dropdownList.add(eventCanal);
                }
                ArrayAdapter<EventCanal> adapter = new ArrayAdapter<EventCanal>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dropdownList);
                dropdown.setAdapter(adapter);
            }
        });
    }

    @Override
    public void postEventCallbackSuccess(){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                HomeActivity.displayActivity((MainActivity) getActivity(), user, "");
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
        datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
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
            timePickerDialog = new TimePickerDialog(getContext(), timeSetListener, lastSelectedHour, lastSelectedMinute, true);
        // Show
        timePickerDialog.show();
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
