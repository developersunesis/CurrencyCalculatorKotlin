package com.android.assessment.currencycalculator.interfaces

import retrofit2.Call
import retrofit2.http.Query
import retrofit2.http.GET

/*Signature: Uche Emmanuel
 * Developersunesis*/

interface ConversionService {

    /**
     *
     * @param accessKey = your access key obtained for fixer.io dashboard
     * @param fromCurrency = the currency converting from
     * @param toCurrency = the currency converting to
     * @param value = the amount/value to be converted
     * @return = return Call<String> of the api result
    </String> */

    @GET("api/convert")
    fun convertCurrency(@Query("access_key") accessKey: String, @Query("from") fromCurrency: String, @Query("to") toCurrency: String, @Query("amount") value: String): Call<String>
}
