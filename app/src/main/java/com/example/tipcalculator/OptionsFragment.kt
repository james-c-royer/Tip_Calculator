package com.example.tipcalculator


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import kotlin.math.roundToInt


class OptionsFragment: Fragment() {

    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        // declare views
        val perPersonTotalView = view.findViewById<TextView>(R.id.perPersonTotal)

        // declare spinners
        val roundingSpinner = view.findViewById<Spinner>(R.id.spinner_rounding)
        val splitSpinner = view.findViewById<Spinner>(R.id.spinner_split)


        // set the default selections to index 0
        roundingSpinner.setSelection(0)
        splitSpinner.setSelection(0)

        // Calculates the per-person total
        fun calcPerPerson() {
            // The per-person bill is determined by the bill, and the number of people splitting it
            val numSplits = splitSpinner.selectedItem.toString().toIntOrNull() ?: 1
            val perPerson = (viewModel.total.value ?: 0.0) / numSplits

            // Then, we format it. If we are rounding the total, then round the per-person split
            // If we aren't rounding the total, just grab the total per person and format for money
            if (roundingSpinner.selectedItem.toString().trim() == "Total"){
                perPersonTotalView.text = perPerson.roundToInt().toString()
            } else {
                perPersonTotalView.text = String.format("%.2f", perPerson)
            }
        }

        // These next two listeners + the observer update the per-person total
        // whenever the spinner/slider/or bill selections are changed
        viewModel.total.observe(viewLifecycleOwner){
            calcPerPerson()
        }

        roundingSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parentView: AdapterView<*>?, p1: View?, spinner_position: Int, p3: Long) {
                val selected = roundingSpinner.selectedItem.toString()
                android.util.Log.d("SPINNER", "Selected: '$selected'")
                viewModel.rounding.value = roundingSpinner.selectedItem.toString()
                calcPerPerson()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                calcPerPerson()
            }
        }

        splitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, p1: View?, spinner_position: Int, p3: Long) {
                calcPerPerson()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                calcPerPerson()
            }
        }
    }
}
