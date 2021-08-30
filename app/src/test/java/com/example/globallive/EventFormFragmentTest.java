package com.example.globallive;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import com.example.globallive.controllers.EventEditActivity;
import com.example.globallive.controllers.UserFormActivity;
import com.example.globallive.entities.Event;
import com.example.globallive.entities.User;
import com.example.globallive.services.App;
import com.example.globallive.services.EventServiceImplementation;
import com.example.globallive.services.IEventService;
import com.example.globallive.tabs.EventFormFragment;
import com.example.globallive.tabs.EventListFragment;
import com.example.globallive.threads.EventListThread;
import com.example.globallive.threads.IEventListCallback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class EventFormFragmentTest
{

    //... declare members
    private Context mContext;
    private UserFormActivity mUserFormActivity;

    @Before
    public void setup(){
        mContext = UserFormActivity.getTargetContext();

        mUserFormActivity = mock(UserFormActivity.class);

        mUserFormActivity.btnSubmit = new Button(mContext);
        mUserFormActivity.userLastName = new TextView(mContext);

        when(mUserFormActivity.getTargetContext()).thenReturn(mContext);
    }
}