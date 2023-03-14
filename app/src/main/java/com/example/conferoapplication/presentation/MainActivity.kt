package com.example.conferoapplication.presentation

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.example.conferoapplication.Utilities.MyReceiver
import com.example.conferoapplication.R
import com.example.conferoapplication.Utilities.Links
import com.example.conferoapplication.Utilities.Resource
import com.example.conferoapplication.Utilities.Utility
import com.example.conferoapplication.data.model.Rates
import com.example.conferoapplication.databinding.ActivityMainBinding
import com.example.conferoapplication.presentation.vm.ExchangeVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private val receiver = MyReceiver()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val vm: ExchangeVM by viewModels()
    private var cur1: String? = null
    private var cur2: String? = null
    private var num1: String? = null
    private var num2: String? = null
    private lateinit var progress: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(receiver, intentFilter)

        initSpinner()


        onDoneClickListener()

        val SwapBt = binding.swapButton
        SwapBt.setOnClickListener {
            Swap()
        }

        progress = binding.progressBar


    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)

        savedInstanceState.putString("cur1", cur1)
        savedInstanceState.putString("cur2", cur2)
        savedInstanceState.putString("num1", num1)
        savedInstanceState.putString("num2", num2)


    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.let { super.onRestoreInstanceState(it) }

        cur1 = savedInstanceState.getString("cur1")
        cur2 = savedInstanceState.getString("cur2")
        num1 = savedInstanceState.getString("num1")
        num2 = savedInstanceState.getString("num2")

        setParameters()

    }


    private fun onDoneClickListener() {
        binding.buttonDone.setOnClickListener {

            this.let { Utility.hideKeyboard(it) }


            val numberToConvert = binding.row1.editTextNumber.text.toString()

            if (numberToConvert.isEmpty() || numberToConvert == "0") {
                showSnackbar("Empty input")
            }

            //check if internet is available
            else if (!Utility.isNetworkAvailable(this)) {
                showSnackbar("Internet unavailable")
            }

            //convert the value

            else {
                showSuccessDialog(window.decorView.rootView)
                doConversion()
            }
        }
    }


    private fun showSuccessDialog(view: View) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_layout)
        dialog.show()


        view.delayOnLifecycle(1000L) {
            dialog.dismiss()
        }
    }


    private fun View.delayOnLifecycle(
        durationInMillis: Long,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        block: () -> Unit
    ): Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
        lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
            delay(durationInMillis)
            block()
        }
    }


    @SuppressLint("UseRequireInsteadOfGet")
    private fun showSnackbar(text: String) {
        Snackbar.make(binding.ExchangeLayout, text, Snackbar.LENGTH_LONG)
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .background(ContextCompat.getColor(this, R.color.red))
            .show()
    }


    private fun setParameters() {

        binding.row1.textCurrency.text = cur1
        binding.row2.textCurrency.text = cur2

        if (num1 != null) binding.row1.editTextNumber.setText(num1.toString())
        if (num2 != null) binding.row2.editTextNumber.setText(num2.toString())
    }

    private fun Swap() {
        cur1 = cur2.also {
            cur2 = cur1
        }


        num1 = binding.row1.editTextNumber.text.toString()
        num2 = binding.row2.editTextNumber.text.toString()



        num1 = num2.also {
            num2 = num1
        }


        setParameters()


    }


    private fun initSpinner() {
        //test array
        val currenc = arrayOf("EUR", "USD", "RUB", "SEK", "ANG", "BYN", "COP", "PLN", "UAH","YER","VND","PHP","ISK","KHR","BRL","IDR", "SOS")

        val spinner_1 = binding.row1.currenciesSpinner
        val spinner_2 = binding.row2.currenciesSpinner
        val arrayAdapter = ArrayAdapter<String>(
            this,
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

    private fun Snackbar.background(color: Int): Snackbar {
        this.view.setBackgroundColor(color)
        return this
    }


    private fun doConversion() {

        progress.visibility = View.VISIBLE


        val apiKey = Links.API_KEY
        val from = cur1.toString()
        val to = cur2.toString()
        val amount = binding.row1.editTextNumber.text.toString().toDouble()

        //do the conversion
        vm.getConvertedData(apiKey, from, to, amount)

        //observe for changes in UI
        observeUi()
    }

    @SuppressLint("SetTextI18n", "UseRequireInsteadOfGet")
    private fun observeUi() {


        vm.data.observe(this, androidx.lifecycle.Observer { result ->

            when (result.status) {
                Resource.Status.SUCCESS -> {
                    if (result.data?.status == "success") {

                        val map: Map<String, Rates>

                        map = result.data.rates
                        map.keys.forEach {

                            val convertedValue = map[it]?.rate_for_amount
                            vm.convertedRate.value = convertedValue

                            //Formatting output
                            Locale.setDefault(Locale.ROOT);
                            val finalString = String.format("%,.2f", vm.convertedRate.value)

                            binding.row2.editTextNumber.setText(finalString)
                        }
                        progress.visibility = View.GONE
                    } else if (result.data?.status == "fail") {

                        progress.visibility = View.GONE
                        showSnackbar("ERROR")

                    }
                }
                Resource.Status.ERROR -> {
                    progress.visibility = View.GONE
                    showSnackbar("ERROR")
                }


                Resource.Status.LOADING -> {
                    progress.visibility = View.GONE
                }
            }
        })

    }






}