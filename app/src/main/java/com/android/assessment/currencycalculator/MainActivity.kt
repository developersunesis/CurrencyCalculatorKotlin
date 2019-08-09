package com.android.assessment.currencycalculator

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast

import com.android.assessment.currencycalculator.database.AppDatabaseClient
import com.android.assessment.currencycalculator.interfaces.ConversionService
import com.android.assessment.currencycalculator.interfaces.SymbolService
import com.android.assessment.currencycalculator.models.SymbolEventBus
import com.android.assessment.currencycalculator.models.Symbols
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

/*Signature: Uche Emmanuel
 * Developersunesis*/

/**
 * The galaxy class
 */

class MainActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    /*nav_state controls the visibility of the NavView*/
    private var nav_state = 0

    /*Views declaration - start*/
    private lateinit var convert: Button

    private lateinit var nav_button: ImageButton
    private lateinit var close: ImageButton
    private lateinit var closeChart: ImageButton

    private lateinit var fromCurrency: LinearLayout
    private lateinit var toCurrency: LinearLayout

    private lateinit var chartView: NavigationView
    private lateinit var navigationView: NavigationView

    /*EditText*/
    private lateinit var fromEditText: EditText
    private lateinit var toEditText: EditText
    private lateinit var subscribeEmail: EditText

    /*TextViews*/
    private lateinit var fromCurrencyText: TextView
    private lateinit var fromCurrencyText1: TextView
    private lateinit var toCurrencyText: TextView
    private lateinit var toCurrencyText1: TextView

    /* Chart View */
    private var chart: LineChart? = null

    /*Views declaration - end*/

    //Declare the symbol variable to hold data from database
    private lateinit var symbolsList: List<Symbols>


    //Set up Retrofit
    private var retrofit = Retrofit.Builder()
            .baseUrl("http://data.fixer.io/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    //This event is to ensure the user gets the updated list of symbols
    @Subscribe
    fun onEvent(symbolEventBus: SymbolEventBus) {

        if (symbolEventBus.isSuccess) {

            LoadSymbols(symbolEventBus.response).execute()

            prefManager.isFirstLaunch = false

            convert.text = getString(R.string.convert)
            convert.setBackgroundDrawable(resources.getDrawable(R.drawable.button_bg))
            convert.isEnabled = true
        } else {
            // Please use loadSymbols(); to locally do that
            loadCurrencySymbols()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //shoot the serviceIntent
        val serviceIntent = Intent(this@MainActivity, SyncSymbolService::class.java)
        startService(serviceIntent)

        symbolsList = ArrayList()

        //initialize the PrefManager.class
        prefManager = PrefManager(this)

        /*Initialize all view elements - start*/
        convert = findViewById(R.id.convert)
        nav_button = findViewById(R.id.nav_button)
        close = findViewById(R.id.close)
        closeChart = findViewById(R.id.closeChart)

        fromEditText = findViewById(R.id.fromEditText)
        toEditText = findViewById(R.id.toEditText)
        subscribeEmail = findViewById(R.id.subscribeEmail)

        navigationView = findViewById(R.id.navigationView)

        fromCurrency = findViewById(R.id.fromCurrency)
        toCurrency = findViewById(R.id.toCurrency)
        chartView = findViewById(R.id.chartView)

        /*initialize TextView - Start*/
        fromCurrencyText = findViewById(R.id.fromCurrencyText)
        fromCurrencyText1 = findViewById(R.id.fromCurrencyText1)

        toCurrencyText = findViewById(R.id.toCurrencyText)
        toCurrencyText1 = findViewById(R.id.toCurrencyText1)
        /*initialize TextView - End*/

        chart = findViewById(R.id.chart)
        /*Initialize all view elements - stop*/


        initViews()
    }


    private fun navStateTrigger() {
        if (nav_state == 0) {
            navigationView.visibility = View.VISIBLE
            nav_state = 1
        } else {
            navigationView.visibility = View.GONE
            nav_state = 0
        }
    }


    private fun initViews() {
        //if first launch disable the convert button until symbols are ready
        if (prefManager.isFirstLaunch) {
            convert.text = getString(R.string.loading)
            convert.setBackgroundDrawable(resources.getDrawable(R.drawable.button_bg_disabled))
            convert.isEnabled = false
        }

        nav_button.setOnClickListener { navStateTrigger() }

        close.setOnClickListener { navStateTrigger() }

        closeChart.setOnClickListener { chartView.visibility = View.GONE }


        findViewById<View>(R.id.subscribeButton).setOnClickListener {
            if (subscribeEmail.text.toString().isNotEmpty()) {
                if (ValidateEmail.isValidEmail(subscribeEmail.text.toString())) {
                    Toast.makeText(applicationContext, "Subscription completed!", Toast.LENGTH_LONG).show()
                    subscribeEmail.setText("")
                } else {
                    Toast.makeText(applicationContext, "Please enter a valid email address!", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(applicationContext, "Please enter your email address!", Toast.LENGTH_LONG).show()
            }
        }

        findViewById<View>(R.id.chartViewButton).setOnClickListener {
            navStateTrigger()
            chartView.visibility = View.VISIBLE
        }

        findViewById<View>(R.id.documentation).setOnClickListener {
            val url = "https://github.com/developersunesis/Currency-Calculator"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        findViewById<View>(R.id.signup).setOnClickListener { startActivity(Intent(applicationContext, SignUpActivity::class.java)) }

        fromCurrencyText1.text = prefManager.lastSavedFrom
        fromCurrencyText.text = prefManager.lastSavedFrom

        toCurrencyText1.text = prefManager.lastSavedTo
        toCurrencyText.text = prefManager.lastSavedTo

        fromCurrency.setOnClickListener { view -> contextMenuDisplay(view, FROM) }

        toCurrency.setOnClickListener { view -> contextMenuDisplay(view, TO) }

        convert.setOnClickListener { convertCurrency(fromEditText, toEditText) }

        //loadCurrencySymbols();

        //finally populate the chart
        setChartData()
    }

    private fun loadCurrencySymbols() {
        val symbolService = retrofit.create(SymbolService::class.java)
        val symbols = symbolService.obtainSymbols()

        symbols.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        LoadSymbols(response.body().toString()).execute()
                    }
                } else {
                    Toast.makeText(applicationContext, "Ops! Something went wrong", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                prefManager.isFirstLaunch = false

                convert.text = getString(R.string.convert)
                convert.setBackgroundDrawable(resources.getDrawable(R.drawable.button_bg))
                convert.isEnabled = true

                //use the saved currency symbols
                LoadSymbols(Constants.SAVED_CURRENCIES_SYMBOLS).execute()

                Snackbar.make(convert, "Please connect to the internet to get updated currency rates!", Snackbar.LENGTH_INDEFINITE).setAction(
                        "Retry"
                ) { loadCurrencySymbols() }.show()
            }
        })
    }


    private fun contextMenuDisplay(view: View, which: Int) {
        if (symbolsList.isNotEmpty()) {
            val popupMenu = PopupMenu(this, view)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                popupMenu.gravity = Gravity.BOTTOM
            }

            for (symbol in symbolsList) {
                popupMenu.menu.add(symbol.name)
            }

            popupMenu.setOnMenuItemClickListener { menuItem ->
                if (which == FROM) {
                    fromCurrencyText.text = menuItem.title
                    fromCurrencyText1.text = menuItem.title

                    prefManager.saveLastFromCurrency(menuItem.title.toString())
                } else {
                    toCurrencyText.text = menuItem.title
                    toCurrencyText1.text = menuItem.title
                    prefManager.saveLasToCurrency(menuItem.title.toString())
                }

                false
            }

            popupMenu.show()
        } else {
            Toast.makeText(applicationContext, "Ops! Something went wrong", Toast.LENGTH_LONG).show()
        }
    }


    /**
     * This method is to convertCurrency
     * @param inputEditText = from currency symbol
     * @param outputEditText = to currency symbol
     */
    private fun convertCurrency(inputEditText: EditText, outputEditText: EditText) {
        val fromString = fromCurrencyText.text.toString()
        val toString = toCurrencyText.text.toString()
        val value = inputEditText.text.toString()

        //provided value != empty please proceed
        if (!value.isEmpty()) {
            val conversionService = retrofit.create<ConversionService>(ConversionService::class.java!!)
            val convertCurrency = conversionService.convertCurrency(
                    Constants.ACCESS_KEY, fromString, toString, value
            )

            convertCurrency.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            var result = "No response!"
                            try {
                                val obj = JSONObject(response.body()!!)

                                result = obj.getDouble("result").toString()
                            } catch (e: JSONException) {
                                Toast.makeText(applicationContext, "Ops! Something went wrong", Toast.LENGTH_LONG).show()
                            }

                            outputEditText.setText(result)
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(applicationContext, "Ops! Something went wrong\nPlease check your internet connection!", Toast.LENGTH_LONG).show()
                }
            })
        } else {

            //gracefully toast the user
            Toast.makeText(applicationContext, "Please enter an amount to be converted!", Toast.LENGTH_LONG).show()
        }
    }


    /*This method is to populate the chart with demo random data*/
    private fun setChartData() {
        chart!!.setViewPortOffsets(0f, 0f, 0f, 0f)
        chart!!.setBackgroundColor(resources.getColor(R.color.colorPrimary))

        //Disable description
        chart!!.description.isEnabled = false

        //Enable the chartView touch
        chart!!.setTouchEnabled(true)

        //Enable the chartView Scale and Drag
        chart!!.isDragEnabled = true
        chart!!.setScaleEnabled(true)

        //Enable pinch zoom
        chart!!.setPinchZoom(true)

        //Disable GridBackground
        chart!!.setDrawGridBackground(false)
        chart!!.maxHighlightDistance = 300f

        chart!!.legend.isEnabled = false

        //Smooth animate data on the chart
        chart!!.animateXY(2000, 2000)

        //Refresh the view
        chart!!.invalidate()


        //Generate random values for the chart
        /*
        * This is just for the demo
        * On a live production the values are to be obtained from fixer.io
        * You would have to subscribe for an above Basic package to get the api enabled
        * to serve the values
         */
        val values = ArrayList<Entry>()

        //Limit to 5 points/dots
        for (i in 0..4) {
            val `val` = (Math.random() * 7).toFloat() + 20
            values.add(Entry(i.toFloat(), `val`))
        }

        val set: LineDataSet

        if (chart!!.data != null && chart!!.data.dataSetCount > 0) {

            set = chart!!.data.getDataSetByIndex(0) as LineDataSet
            set.values = values
            chart!!.data.notifyDataChanged()
            chart!!.notifyDataSetChanged()

        } else {
            // create a new data set and give it a type
            set = LineDataSet(values, "Currency Chart")

            //define custom attributes
            set.mode = LineDataSet.Mode.CUBIC_BEZIER
            set.cubicIntensity = 0.2f
            set.setDrawFilled(true)
            set.setDrawCircles(true)
            set.lineWidth = 0.5f
            set.circleRadius = 5f
            set.circleHoleRadius = 3f
            set.setCircleColor(resources.getColor(R.color.white))
            set.circleHoleColor = resources.getColor(R.color.colorAccent)
            set.highLightColor = resources.getColor(R.color.chartIndicator)
            set.color = resources.getColor(R.color.chartIndicator)
            set.fillColor = resources.getColor(R.color.chartIndicator)
            set.fillAlpha = 100
            set.setDrawHorizontalHighlightIndicator(true)

            set.fillFormatter = IFillFormatter { dataSet, dataProvider -> chart!!.axisLeft.axisMinimum }

            //create a data object with the data sets
            val data = LineData(set)
            data.setValueTextColor(resources.getColor(R.color.white))
            data.setValueTextSize(9f)
            data.setDrawValues(true)

            //set data to chart
            chart!!.data = data
        }
    }


    /**
     * LoadSymbols AsyncTask to obtain and refresh the currencies symbols
     */
    private inner class LoadSymbols
    /**
     * @param responseBody: Load the response parameter to be used to populate the dropdown menus
     */
    internal constructor(var responseBody: String) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            try {
                val obj = JSONObject(responseBody).getJSONObject("symbols")

                val objNames = obj.names()
                for (i in 0 until (objNames?.length() ?: 0)) {
                    val name = objNames!!.getString(i)
                    val abbr = obj.getString(name)

                    val symbol = Symbols()
                    symbol.name = name
                    symbol.symbol = abbr
                    AppDatabaseClient.getInstance(applicationContext)
                            .symbolsDao().insertSymbol(symbol)
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            symbolsList = AppDatabaseClient.getInstance(applicationContext)
                    .symbolsDao().allSymbols

            return null
        }

        public override fun onPostExecute(avoid: Void?) {
            super.onPostExecute(avoid)

            prefManager.isFirstLaunch = false

            convert.text = getString(R.string.convert)
            convert.setBackgroundDrawable(resources.getDrawable(R.drawable.button_bg))
            convert.isEnabled = true
        }
    }

    companion object {

        /*The ints below is to control which step to take onContextMenu*/
        private const val FROM = 0
        private const val TO = 1
    }
}
