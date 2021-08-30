package com.example.globallive;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

import android.os.Handler;
import com.example.globallive.entities.Event;
import com.example.globallive.entities.User;
import com.example.globallive.services.App;
import com.example.globallive.services.EventServiceImplementation;
import com.example.globallive.services.IEventService;
import com.example.globallive.tabs.EventListFragment;
import com.example.globallive.threads.EventListThread;
import com.example.globallive.threads.IEventListCallback;
import java.util.Arrays;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class EventListTest
{
    @Mock
    User user;

    @Mock
    private EventListThread eventListThread;

    @Mock
    private Handler handler = mock(Handler.class);

    @Mock
    private EventServiceImplementation eventServiceImplementation = mock(EventServiceImplementation.class);

    private EventListFragment eventListFragment;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        eventListFragment = new EventListFragment(user, eventServiceImplementation, handler);
    }


    @Test
    public void testDoSomethingAsynchronouslyUsingDoAnswer() throws Exception {
        //Objet factice
        final List<Event> resultsEvents = Arrays.asList(new Event(), new Event());

        // Prépare une réponse synchrone pour notre callback
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IEventListCallback)invocation.getArguments()[0]).callbackSuccess(resultsEvents);
                return null;
            }
        }).when(eventListThread).run();

        // Appel la méthode
        eventListFragment.updateEvents(0,0);

        // Vérifie l'intéraction
        verify(eventListFragment, times(1)).callbackSuccess(anyList());
        assertThat(eventListFragment.getResult(), is(equalTo(resultsEvents)));

    }

}