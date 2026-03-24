package com.example.tipcalculator
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.slider.Slider
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Grab our views
        val slider = findViewById<Slider>(R.id.slider)
        val percentView = findViewById<TextView>(R.id.percentageView)
        val tipResult = findViewById<TextView>(R.id.tip_amount) // holds the actual amount we want to tip
        val totalBillView = findViewById<TextView>(R.id.total_bill)
        val billView = findViewById<EditText>(R.id.bill_amount)


        // set the default percent value
        percentView.text = "${slider.value.toInt()}" + "%"
        // Create the top toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.materialToolBar)
        toolbar.setTitleTextColor(Color.WHITE) // defines the text color
        setSupportActionBar(toolbar)

        // Create the bottom nav bar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavView.setupWithNavController(navController)

        // reference our view model
        val viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        // These functions are called whenever we change the bill amount or the slider.
        // They look at if the appropriate rounding option is selected, and format it appropriately.
        fun formatTotal(total: Double): String {
            return if (viewModel.rounding.value == "Total"){
                total.roundToInt().toString()
            } else {
                String.format("%.2f", total)
            }
        }

        fun formatTip(tip: Double): String {
            return if (viewModel.rounding.value == "Tip"){
                tip.roundToInt().toString()
            } else {
                String.format("%.2f", tip)
            }
        }

        // if the rounding spinner item selection is changed,
        // we need to call the appropriate formatters
        viewModel.rounding.observe(this) { roundingValue ->
            val total = calcTotal(slider.value.toInt(), billView)
            totalBillView.text = formatTotal(total)

            viewModel.total.value = total
            val tip = calcTip(slider.value.toInt(), billView)
            tipResult.text = formatTip(tip)
        }

        // The next two functions ensure that the tip and totals are accurately calculated
        // whenever the bill total is changed, or the percent tip is changed, we call
        // calcTip to get the new tip value, calcTotal to get the new total,
        // and the appropriate formatters to display the output.
        billView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){
                val total = calcTotal(slider.value.toInt(), billView)
                val tip = calcTip(slider.value.toInt(), billView)
                tipResult.text = formatTip(tip)
                totalBillView.text = formatTotal(total)
                viewModel.total.value = total
            }

            // these overrides are required, but we don't actually need them
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){
            }

            override fun afterTextChanged(s: Editable?){
            }
        })

        // Listens to when the slider is changed
        slider.addOnChangeListener { _, value, _ ->
            val total = calcTotal(slider.value.toInt(), billView)
            val tip = calcTip(slider.value.toInt(), billView)
            percentView.text = value.toInt().toString() + "%"
            tipResult.text = formatTip(tip)
            totalBillView.text = formatTotal(total)
            viewModel.total.value = total
        }
    }

    // calculates tip
    fun calcTip(tipPercent: Int, billView: EditText): Double {
        val bill = billView.text.toString().toDoubleOrNull() ?: 0.0
        return (bill / 100 * tipPercent)
    }

    // multiply the bill amount by the percent to get the tip
    fun calcTotal(tipPercent: Int, billView: EditText): Double {
        val bill = billView.text.toString().toDoubleOrNull() ?: 0.0
        return bill + (bill / 100 * tipPercent)
    }
}