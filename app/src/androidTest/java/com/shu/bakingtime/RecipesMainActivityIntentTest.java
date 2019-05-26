package com.shu.bakingtime;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.espresso.contrib.RecyclerViewActions;

import static org.hamcrest.CoreMatchers.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipesMainActivityIntentTest {

    @Rule
    public IntentsTestRule<RecipesMainActivity> mainActivityIntentsTestRule
            = new IntentsTestRule<>(RecipesMainActivity.class);


    @Test
    public void clickingRecipeCard_validRecipeData_launchesRecipeActivity_2() {
        onView(withId(R.id.recipe_list_container))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(allOf(
                hasExtraWithKey(RecipesMainActivity.EXT_RECIPE),
                hasComponent(hasShortClassName(".RecipeActivity")))
        );
    }

}



