package com.nbrk.rates.ui.main;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.nbrk.rates.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

  @Test
  public void mainActivityTest() {
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction appCompatImageButton = onView(
      allOf(withContentDescription("Open navigation drawer"),
        childAtPosition(
          allOf(withId(R.id.toolbar),
            childAtPosition(
              withClassName(is("android.support.design.widget.AppBarLayout")),
              0)),
          1),
        isDisplayed()));
    appCompatImageButton.perform(click());

    ViewInteraction navigationMenuItemView = onView(
      allOf(childAtPosition(
        allOf(withId(R.id.design_navigation_view),
          childAtPosition(
            withId(R.id.nav_view),
            0)),
        1),
        isDisplayed()));
    navigationMenuItemView.perform(click());

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction recyclerView = onView(
      allOf(withId(R.id.ratesList),
        childAtPosition(
          allOf(withId(R.id.lRefresh),
            childAtPosition(
              IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
              0)),
          0),
        isDisplayed()));
    recyclerView.check(matches(isDisplayed()));

  }

  private static Matcher<View> childAtPosition(
    final Matcher<View> parentMatcher, final int position) {

    return new TypeSafeMatcher<View>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("Child at position " + position + " in parent ");
        parentMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view) {
        ViewParent parent = view.getParent();
        return parent instanceof ViewGroup && parentMatcher.matches(parent)
          && view.equals(((ViewGroup) parent).getChildAt(position));
      }
    };
  }
}
