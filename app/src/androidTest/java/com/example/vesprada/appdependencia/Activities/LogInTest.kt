package com.example.vesprada.appdependencia.Activities


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import com.example.vesprada.appdependencia.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LogInTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SplashInitActivity::class.java)

    @Test
    fun logInTest() {

        //TEST LOGIN ERROR AL LOGEAR
        val appCompatEditText_le = onView(
                allOf(withId(R.id.et_DependentDNI),
                        childAtPosition(
                                allOf(withId(R.id.loginLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()))
        appCompatEditText_le.perform(replaceText("2k"), closeSoftKeyboard())



        val appCompatEditText2_le = onView(
                allOf(withId(R.id.et_password),
                        childAtPosition(
                                allOf(withId(R.id.loginLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()))
        appCompatEditText2_le.perform(replaceText("1234"), closeSoftKeyboard())

        val appCompatButton_le = onView(
                allOf(withId(R.id.bt_register), withText("Log In"),
                        childAtPosition(
                                allOf(withId(R.id.loginLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()))
        appCompatButton_le.perform(click())

        onView(withId(R.id.tvlog)).check(matches(withText("LoginIncorrect")))

        //TEST LOGIN CORRECTO

        val appCompatEditText_lw = onView(
                allOf(withId(R.id.et_DependentDNI),
                        childAtPosition(
                                allOf(withId(R.id.loginLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()))
        appCompatEditText_lw.perform(replaceText("28888810k"), closeSoftKeyboard())

        val appCompatEditText2_lw = onView(
                allOf(withId(R.id.et_password),
                        childAtPosition(
                                allOf(withId(R.id.loginLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()))
        appCompatEditText2_lw.perform(replaceText("1234"), closeSoftKeyboard())

        val appCompatButton_lw = onView(
                allOf(withId(R.id.bt_register), withText("Log In"),
                        childAtPosition(
                                allOf(withId(R.id.loginLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()))
        appCompatButton_lw.perform(click())

        onView(withId(R.id.tvCorrect)).check(matches(withText("correct")))
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
