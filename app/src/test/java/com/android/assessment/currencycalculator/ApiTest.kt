package com.android.assessment.currencycalculator

import android.util.Log

import com.android.assessment.currencycalculator.interfaces.ConversionService
import com.android.assessment.currencycalculator.interfaces.SymbolService

import org.junit.Test

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue

class ApiTest {

    internal var retrofit = Retrofit.Builder()
            .baseUrl("http://data.fixer.io/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

    //Convert 50USD to NGN runs successfully
    @Test
    fun symbolsObtainJSON() {
        val symbolService = retrofit.create(SymbolService::class.java)
        val symbols = symbolService.obtainSymbols()
        symbols.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                assertNotNull(response.body())
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                assertFalse(false)
            }
        })
    }

    @Test
    fun convert50USDtoNGN() {
        val conversionService = retrofit.create(ConversionService::class.java)
        val convertCurrency = conversionService.convertCurrency(
                Constants.ACCESS_KEY, "USD", "NGN", "50"
        )

        convertCurrency.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                assertNotNull(response.body())
                Log.e("Convert 50USD to NGN result", response.body()!!)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                assertFalse(false)
            }
        })
    }
}
