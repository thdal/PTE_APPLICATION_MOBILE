package com.example.globallive.tabs;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.globallive.R;
import com.example.globallive.entities.OperationSuccess;

public class EventFormFragment extends Fragment {

    private Button submitEventButton;
    private OperationSuccess _registrationSuccess;

    private Handler _mainHandler = new Handler();

    public EventFormFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_form, container, false);
    }

}
