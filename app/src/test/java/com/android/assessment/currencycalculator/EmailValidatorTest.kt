package com.android.assessment.currencycalculator

import org.junit.Test

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

/**
 * Email address validation
 */
class EmailValidatorTest {

    @Test
    fun email_isWellformed() {
        assertTrue(ValidateEmail.isValidEmail("developersunesis@gmail.com"))
    }

    @Test
    fun email_isNotWellformed() {
        assertFalse(ValidateEmail.isValidEmail("developersunesis@gmail."))
    }
}
