package com.android.assessment.currencycalculator.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

import com.android.assessment.currencycalculator.models.Symbols

/*Signature: Uche Emmanuel
 * Developersunesis*/

/**
 * SymbolsDao
 * to carry out SQLite operations CRUD
 * insert, delete and update
 */
@Dao
interface SymbolsDao {

    @get:Query("SELECT * FROM symbols")
    val allSymbols: List<Symbols>

    /**
     * This inserts a Symbols model to the database
     * @param symbol = a model
     */
    @Insert
    fun insertSymbol(symbol: Symbols)

    /**
     * This delete a Symbols model to the database
     * @param symbol = a model
     */
    @Delete
    fun deleteSymbol(symbol: Symbols)

    /**
     * This update a Symbols model to the database
     * @param symbol = a model
     */
    @Update
    fun updateSymbol(symbol: Symbols)
}
