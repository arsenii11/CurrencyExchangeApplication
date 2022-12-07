package com.example.conferoapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.conferoapplication.Utilities.Links
import com.example.conferoapplication.Utilities.Resource
import com.example.conferoapplication.Utilities.Utility
import com.example.conferoapplication.databinding.FragmentExchangeBinding
import com.example.conferoapplication.model.Rates
import com.example.conferoapplication.vm.ExchangeVM
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class ExchangeDialog : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentExchangeBinding
    private val vm: ExchangeVM by viewModels()
    private var cur1: String? = null
    private var cur2: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentExchangeBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()

        setOnClickListener()


        val SwapBt = binding.swapButton
        SwapBt.setOnClickListener {
            Swap()
        }
    }


    private fun Swap() {
        cur1 = cur2.also {
            cur2 = cur1
        }

        binding.row1.textCurrency.text = cur1
        binding.row2.textCurrency.text = cur2

        var num1 = binding.row1.editTextNumber.text.toString()
        var num2 = binding.row2.editTextNumber.text.toString()

        num1 = num2.also {
            num2 = num1
        }

        binding.row1.editTextNumber.setText(num1.toString())
        binding.row2.editTextNumber.setText(num2.toString())


    }


    private fun initSpinner() {

    //test array
        val currenc = arrayOf("EUR", "USD", "RUB", "SEK")

        val spinner_1 = binding.row1.currenciesSpinner
        val spinner_2 = binding.row2.currenciesSpinner
        val arrayAdapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            currenc
        )


        spinner_1.adapter = arrayAdapter
        spinner_2.adapter = arrayAdapter

        spinner_1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val cur_text_1 = binding.row1.textCurrency
                cur_text_1.text = currenc[position]
                cur1 = currenc[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        spinner_2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val cur_text_2 = binding.row2.textCurrency
                cur_text_2.text = currenc[position]
                cur2 = currenc[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }


    }


    /**
     * A method for handling click events in the UI
     */

    private fun setOnClickListener() {

        binding.buttonDone.setOnClickListener {


            val numberToConvert = binding.row1.editTextNumber.text.toString()

            if (numberToConvert.isEmpty() || numberToConvert == "0") {


                Toast.makeText(activity, "Internet unavailable", Toast.LENGTH_SHORT).show()
            }

            //check if internet is available
            else if (!Utility.isNetworkAvailable(activity)) {
                Toast.makeText(activity, "Internet unavailable", Toast.LENGTH_SHORT).show()
            }

            //carry on and convert the value
            else {
                doConversion()
            }
        }
    }


    private fun doConversion() {



        /* Utility.hideKeyboard(binding)*/


        val apiKey = Links.API_KEY
        val from = cur1.toString()
        val to = cur2.toString()
        val amount = binding.row1.editTextNumber.text.toString().toDouble()

        //do the conversion
        vm.getConvertedData(apiKey, from, to, amount)

        //observe for changes in UI
        observeUi()
    }

    @SuppressLint("SetTextI18n")
    private fun observeUi() {


        vm.data.observe(this, androidx.lifecycle.Observer { result ->

            when (result.status) {
                Resource.Status.SUCCESS -> {
                    if (result.data?.status == "success") {

                        val map: Map<String, Rates>

                        map = result.data.rates

                        map.keys.forEach {

                            val rateForAmount = map[it]?.rate_for_amount

                            vm.convertedRate.value = rateForAmount



                            val finalString=  vm.convertedRate.value.toString()


                            binding.row2.editTextNumber.setText(finalString)

                        }
                    } else if (result.data?.status == "fail") {

                    }
                }
                Resource.Status.ERROR -> {

                }

                Resource.Status.LOADING -> {

                }
            }
        })

    }

}



