package com.android.assessment.currencycalculator.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import java.io.Serializable

/*Signature: Uche Emmanuel
 * Developersunesis*/

/**
 * This is a Symbol Model
 */

@Entity
class Symbols : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "symbol")
    var symbol: String? = null

    @ColumnInfo(name = "name")
    var name: String? = null
}
