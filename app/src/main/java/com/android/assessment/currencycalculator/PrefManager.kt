package com.android.assessment.currencycalculator

import android.content.Context
import android.content.SharedPreferences

/*Signature: Uche Emmanuel
 * Developersunesis*/

internal class PrefManager(context: Context) {
    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor

    /**
     * Get the state if already launched before
     * @return = returns a boolean
     */
    /**
     * Set the state is the app is launching foe the first  time
     * @param isFirstLaunch : This would be used to control the API request, it would gracefully pend the user operation to load the currencies symbols
     */
    var isFirstLaunch: Boolean
        get() = pref.getBoolean("isAppFirstLaunch", true)
        set(isFirstLaunch) {
            editor.putBoolean("isAppFirstLaunch", isFirstLaunch)
            editor.commit()
        }

    /**
     * Get the last saved from currency symbol
     * @return = returns a string
     */
    val lastSavedFrom: String
        get() = pref.getString("lastFromCurrency", "USD").toString()

    /**
     * Get the last saved to currency symbol
     * @return = returns a string
     */
    val lastSavedTo: String
        get() = pref.getString("lastToCurrency", "NGN").toString()

    init {
        val PRIVATE_MODE = 0
        pref = context.getSharedPreferences("currencies_pref", PRIVATE_MODE)
        editor = pref.edit()
        editor.apply()
    }

    /**
     * Save the last from currency symbol
     * @param from = set from param
     */
    fun saveLastFromCurrency(from: String) {
        editor.putString("lastFromCurrency", from)
        editor.commit()
    }

    /**
     * Save the last to currency symbol
     * @param to = set from param
     */
    fun saveLasToCurrency(to: String) {
        editor.putString("lastToCurrency", to)
        editor.commit()
    }
}