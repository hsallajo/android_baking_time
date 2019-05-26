package com.shu.bakingtime;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipesMainActivityBasicTest {

    @Rule
    public ActivityTestRule<RecipesMainActivity> mActivity
            = new ActivityTestRule<>(RecipesMainActivity.class);


    @Test
    public void clickOnRecipeCard_OpensNewActivity(){
        onView(withText("Yellow Cake"))
                .perform(click());
        onView(
                withText("Starting prep"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickOnOverflowMenu_DisplaysRefreshMenuItem(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Refresh"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickOnRecipeCard_OpensNewActivityWithUpButton(){
        onView(withText("Brownies"))
                .perform(click());
        onView(withContentDescription("Navigate up"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickOnUpButton_ReturnsToRecipesScreen(){
        onView(withText("Brownies"))
                .perform(click());
        onView(withContentDescription("Navigate up"))
                .perform(click());
        onView(withText("Cheesecake"))
                .check(matches(isDisplayed()));
    }

}
