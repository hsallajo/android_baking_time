package com.shu.bakingtime;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.shu.bakingtime.model.Ingredient;
import com.shu.bakingtime.model.Recipe;
import com.shu.bakingtime.model.Step;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.parceler.Parcels;
import java.util.ArrayList;
import java.util.List;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.shu.bakingtime.RecipesMainActivity.EXT_RECIPE;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityScreenTest {

    @Rule
    public ActivityTestRule<RecipesMainActivity> mMainActivity = new ActivityTestRule<RecipesMainActivity>(RecipesMainActivity.class);

    @Rule
    public ActivityTestRule<RecipeActivity> mRecipeActivity = new ActivityTestRule<RecipeActivity>(RecipeActivity.class, false, false);


    @Test
    public void IngredientsViewIsTopMostDisplayedInStepsRecycleView(){

        onView(withText("Yellow Cake"))
                .perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.lv_ingredients))
                .atPosition(0)
                .check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void launchRecipeActivityWithIntent(){

        Intent i = new Intent();
        i.putExtra(EXT_RECIPE, Parcels.wrap(mockRecipe()));
        mRecipeActivity.launchActivity(i);

        mRecipeActivity.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }

    @Test
    public void launchRecipeActivityInTablet_launchesTwoPaneLayoutWithPlayer(){

        Intent i = new Intent();
        i.putExtra(EXT_RECIPE, Parcels.wrap(mockRecipe()));
        mRecipeActivity.launchActivity(i);
        mRecipeActivity.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if(isTablet(mRecipeActivity.getActivity())){

            onView(withId(R.id.player_fragment_container)).check(matches(isDisplayed()));
        }
    }

    private Recipe mockRecipe() {

        Ingredient in = new Ingredient();
        in.setMeasure("CUP");
        in.setQuantity(2.0f);
        in.setIngredient("Graham Cracker crumbs");

        Step stepFromParent = new Step();
        stepFromParent.setShortDescription("Recipe Introduction");
        stepFromParent.setDescription("Recipe Introduction");
        stepFromParent.setVideoURL("https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4");
        stepFromParent.setId(0);

        List<Ingredient> ingredients = new ArrayList<>();
        List<Step> steps = new ArrayList<>();

        ingredients.add(in);
        ingredients.add(in);
        ingredients.add(in);

        steps.add(stepFromParent);
        steps.add(stepFromParent);
        steps.add(stepFromParent);
        steps.add(stepFromParent);
        steps.add(stepFromParent);

        return new Recipe("Nutella Pie", ingredients, steps, 8, "" );

    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
