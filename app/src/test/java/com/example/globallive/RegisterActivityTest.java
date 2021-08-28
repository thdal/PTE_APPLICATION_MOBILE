package com.example.globallive;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.view.View;

import com.example.globallive.controllers.RegisterActivity;
import com.example.globallive.entities.Event;
import com.example.globallive.threads.IEventListCallback;

import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RegisterActivityTest {
    private int mShowMessageCount;

    @Test
    public void presenter_callView() throws Exception {

       RegisterActivity registerActivity = mock(RegisterActivity.class);

        when(registerActivity.test()).thenReturn("");
        //when(registerActivity.onClick(mock(android.view.View.class));



        IEventListCallback quizzView = new IEventListCallback() {
            @Override
            public void callbackSuccess(List<Event> events) {

            }

            @Override
            public void callbackFail(String msg) {
                mShowMessageCount++;
            }
        };
    }
}