package com.android.assessment.currencycalculator

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4

import com.android.assessment.currencycalculator.database.AppDatabase
import com.android.assessment.currencycalculator.database.AppDatabaseClient

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import junit.framework.TestCase.assertNotNull

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    var mainActivityTestRule = ActivityTestRule(MainActivity::class.java)

    //test views
    @Test
    fun onCreate() {
        onView(withId(R.id.convert))
        onView(withId(R.id.fromEditText))
        onView(withId(R.id.toEditText))
        onView(withId(R.id.fromCurrency))
        onView(withId(R.id.toCurrency))
        //Hit convert button
        onView(withId(R.id.convert)).perform(click())
    }

    //test database connection
    @Test
    fun testDatabase() {
        assertNotNull(AppDatabaseClient.getInstance(mainActivityTestRule.activity))
    }
}