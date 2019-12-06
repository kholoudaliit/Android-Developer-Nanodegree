package com.udacity.gradle.builditbigger;

import android.content.Intent;

import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;


@RunWith(AndroidJUnit4.class)

public class EndpointsAsyncTaskTest {

    MainActivity activity;
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        MainActivityFragment fragment = new MainActivityFragment();
        mainActivityActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, fragment).commit();

    }


    @Test
    public void TestisNotEmptyText() throws Exception {


        EndpointsAsyncTask testTask = new EndpointsAsyncTask(mainActivityActivityTestRule.getActivity());
        testTask.onPreExecute();
        testTask.execute();
        String testJoke = testTask.get();

        assertNotNull(testJoke);
        assertFalse(testJoke.isEmpty());

    }


}