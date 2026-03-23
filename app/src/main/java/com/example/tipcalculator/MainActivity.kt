package com.example.tipcalculator

import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.slider.Slider

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Grab our views
        val slider = findViewById<Slider>(R.id.slider)
        val percentView = findViewById<TextView>(R.id.percentageView)
        val tipView = findViewById<TextView>(R.id.tip_amount)
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

        slider.addOnChangeListener { slider, value, fromUser ->
            // Round the percent to the nearest whole number
            percentView.text = value.toInt().toString() + "%"
            calcTip(tipView, billView)
        }
    }
    // multiply the bill amount by the percent to get the tip
    fun calcTip(tipView: TextView, billView: EditText){
    }
}