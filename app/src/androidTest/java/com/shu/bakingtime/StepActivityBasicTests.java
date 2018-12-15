package com.shu.bakingtime;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.shu.bakingtime.model.Ingredient;
import com.shu.bakingtime.model.Step;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.shu.bakingtime.RecipeActivity.EXT_STEP_DATA;
import static com.shu.bakingtime.RecipeActivity.EXTRA_IS_NEXT_STEP;
import static com.shu.bakingtime.RecipeActivity.EXTRA_IS_PREV_STEP;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class StepActivityBasicTests {

    @Rule
    public ActivityTestRule<StepActivity> mStepActivity = new ActivityTestRule<StepActivity>(StepActivity.class, false, false);

    @Test
    public void launchStepActivityWithIntent_playerIsVisible() {

        mStepActivity.launchActivity(initializeMockIntent());

        mStepActivity.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if (mockStep().getVideoURL() != null) {
            onView(allOf(withId(R.id.playerView), withClassName(is(SimpleExoPlayerView.class.getName())))).check(matches(isDisplayed()));
        }
    }

    @Test
    public void launchStepActivityWithIntent_InstructionsAreVisible() {

        mStepActivity.launchActivity(initializeMockIntent());

        mStepActivity.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        onView(allOf(withId(R.id.tv_instruction)))
                .check(matches(withText(mockStep().getDescription())));

    }

    private Intent initializeMockIntent(){
        Intent i = new Intent();
        i.putExtra(EXT_STEP_DATA, Parcels.wrap(mockStep()));
        i.putExtra(EXTRA_IS_NEXT_STEP, true);
        i.putExtra(EXTRA_IS_PREV_STEP, true);
        return i;
    }

    private Step mockStep() {

        Step stepFromParent = new Step();
        stepFromParent.setShortDescription("Recipe Introduction");
        stepFromParent.setDescription("Recipe Introduction");
        stepFromParent.setVideoURL("https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4");
        stepFromParent.setId(0);

        List<Ingredient> ingredients = new ArrayList<>();
        List<Step> steps = new ArrayList<>();

        steps.add(stepFromParent);
        steps.add(stepFromParent);
        steps.add(stepFromParent);
        steps.add(stepFromParent);
        steps.add(stepFromParent);

        Step s = new Step();
        s.setId(1);
        s.setShortDescription("Prep the cookie crust.");
        s.setDescription("2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.");
        s.setVideoURL("https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4");
        s.setThumbnailURL("");
        return s;
    }
}
