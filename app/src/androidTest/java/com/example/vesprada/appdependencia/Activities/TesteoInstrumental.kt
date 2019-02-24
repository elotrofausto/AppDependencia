package com.example.vesprada.appdependencia.Activities


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import com.example.vesprada.appdependencia.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.widget.EditText
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.test.internal.util.Checks
import android.widget.TextView


@LargeTest
@RunWith(AndroidJUnit4::class)
class TesteoInstrumental {

    //NOTA IMPORTANTE: PARA REALIZAR EL TESTEO EL ARCHIVO SHARED PREFERENCES NO PUEDE EXISTIR,
    // YA QUE SI EXISTE LA LoginActivity NO SE LANZA Y POR LO TANTO NO SE PUEDE PROBAR

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SplashInitActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION")

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



        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(3000)

        onView(withId(R.id.tvCorrect)).check(matches(withText("correct")))

        val bottomNavigationItemView = onView(
                allOf(withId(R.id.notificaciones), withContentDescription("Notifications"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation),
                                        0),
                                1),
                        isDisplayed()))
        bottomNavigationItemView.perform(click())

        onView(withId(R.id.tvCorrect)).check(matches(withText("correct")))

        val bottomNavigationItemView2 = onView(
                allOf(withId(R.id.eventos), withContentDescription("Events"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation),
                                        0),
                                2),
                        isDisplayed()))
        bottomNavigationItemView2.perform(click())

        onView(withId(R.id.tvCorrect)).check(matches(withText("correct")))

        val bottomNavigationItemView3 = onView(
                allOf(withId(R.id.configuracion), withContentDescription("Configuration"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigationH),
                                        0),
                                4),
                        isDisplayed()))
        bottomNavigationItemView3.perform(click())

        onView(withId(R.id.tvCorrect)).check(matches(withText("correct")))

        val appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(Matchers.`is`("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageButton.perform(click())

        onView(withId(R.id.ed_dniDependiente)).check(matches(withTextTag("Night")))

        val navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        2),
                        isDisplayed()))
        navigationMenuItemView.perform(click())

        onView(withId(R.id.tvCorrect)).check(matches(withText("correct")))

        val bottomNavigationItemView4 = onView(
                allOf(withId(R.id.googlemap), withContentDescription("Map"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigationconf),
                                        0),
                                3),
                        isDisplayed()))
        bottomNavigationItemView4.perform(click())

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

    fun withTextTag(tag: String): Matcher<View> {
        Checks.checkNotNull(tag)
        return object : BoundedMatcher<View, EditText>(EditText::class.java) {
            public override fun matchesSafely(warning: EditText): Boolean {
                return tag == warning.tag.toString()
            }

            override fun describeTo(description: Description) {
                description.appendText("with text color: ")
            }
        }
    }
}
