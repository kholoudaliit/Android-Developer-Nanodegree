package com.kholoud.bakingapp;


import android.os.Bundle;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.google.android.exoplayer2.ui.PlayerView;
import com.kholoud.bakingapp.model.Recipe;
import com.kholoud.bakingapp.views.VideoStepsDetailActivity;
import com.kholoud.bakingapp.views.VideoStepsDetailFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;


@RunWith(AndroidJUnit4.class)
public class RecipeDetailUiEspressoTest {


    Recipe recipe;

    @Rule
    public ActivityTestRule<VideoStepsDetailActivity> detailActivityTestRule = new ActivityTestRule<>(VideoStepsDetailActivity.class);


    @Before
    public void setup() {
        VideoStepsDetailFragment fragment = new VideoStepsDetailFragment();
        Bundle b = new Bundle();
        b.putString("des" , "put the cup of milk on oven to get warm");
        b.putString("thumbnailURL", "");
        b.putString("ViedoURL", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4");
        fragment.setArguments(b);
        detailActivityTestRule.getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.items_detail_container, fragment)
                .commit();

    }


    @Test
    public void RecipeStepDesTextDisplayed() {
        onView(withId(R.id.step_des)).check(matches(isDisplayed()));
    }

    @Test
    public void ExpoPlayerisDisplayed() {
        onView(allOf(withId(R.id.exo_play),
                withClassName(is(PlayerView.class.getName())))).check(matches(isDisplayed()));
    }


}

