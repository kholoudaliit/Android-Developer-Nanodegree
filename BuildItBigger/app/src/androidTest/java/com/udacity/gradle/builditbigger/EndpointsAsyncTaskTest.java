package com.udacity.gradle.builditbigger;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;


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
        assertNotEquals("", testJoke);


    }


}