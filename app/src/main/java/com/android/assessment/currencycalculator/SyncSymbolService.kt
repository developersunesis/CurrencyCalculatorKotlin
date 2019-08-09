package com.android.assessment.currencycalculator

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.widget.Toast

import com.android.assessment.currencycalculator.interfaces.SymbolService
import com.android.assessment.currencycalculator.models.SymbolEventBus

import org.greenrobot.eventbus.EventBus

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

/*Signature: Uche Emmanuel
 * Developersunesis*/

class SyncSymbolService : IntentService("SyncSymbolService") {

    internal var retrofit = Retrofit.Builder()
            .baseUrl("http://data.fixer.io/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

    override fun onHandleIntent(intent: Intent?) {
        val symbolService = retrofit.create<SymbolService>(SymbolService::class.java!!)
        val symbols = symbolService.obtainSymbols()

        symbols.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        EventBus.getDefault().post(SymbolEventBus(true, response.body().toString()))
                    }
                } else {
                    EventBus.getDefault().post(SymbolEventBus(false, "Ops! Something went wrong"))
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                EventBus.getDefault().post(SymbolEventBus(false, "Ops! Something went wrong\n Please check your internet connection"))
            }
        })
    }

}
