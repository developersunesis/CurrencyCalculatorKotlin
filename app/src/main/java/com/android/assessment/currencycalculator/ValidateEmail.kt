package com.android.assessment.currencycalculator

import java.util.regex.Pattern

class ValidateEmail {

    val isValid = false

    companion object {

        val EMAIL_PATTERN = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        )


        /**
         * Validates if the given input is a valid email address.
         *
         * @param email        The email to validate.
         * @return `true` if the input is a valid email. `false` otherwise.
         */
        fun isValidEmail(email: CharSequence?): Boolean {
            return email != null && EMAIL_PATTERN.matcher(email).matches()
        }
    }
}
