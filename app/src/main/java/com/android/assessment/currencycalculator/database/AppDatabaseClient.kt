package com.android.assessment.currencycalculator.database

import android.content.Context

import androidx.room.Room

/*Signature: Uche Emmanuel
 * Developersunesis*/

/**
 * AppDatabaseClient class
 * To sustain a database instance
 */
class AppDatabaseClient
/**
 * This method builds the app database 'Currencies'
 * @param context = set the app context
 */
{

    companion object {

        /**
         * This method returns an instance to be used
         * @param context = set method context
         * @return = returns instance
         */
        @Synchronized
        fun getInstance(context: Context)= Room.databaseBuilder(context, AppDatabase::class.java, "Currencies").build()
    }
}
