package com.example.currencyExchangeApplication.presentation

import android.app.Dialog
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.example.conferoapplication.R
import com.example.conferoapplication.databinding.ActivityMainBinding
import com.example.currencyExchangeApplication.data.database.ConversionHistoryEntity
import com.example.currencyExchangeApplication.presentation.utilities.CurrenciesAvailable
import com.example.currencyExchangeApplication.utilities.Links
import com.example.currencyExchangeApplication.presentation.utilities.MyReceiver
import com.example.currencyExchangeApplication.presentation.utilities.Utility
import com.example.currencyExchangeApplication.data.model.Rates
import com.example.currencyExchangeApplication.presentation.components.SnackBar.Companion.showSnackBar
import com.example.currencyExchangeApplication.presentation.history.HistoryActivity
import com.example.currencyExchangeApplication.presentation.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val receiver = MyReceiver()
    private lateinit var binding: ActivityMainBinding

    private var cur1: String? = null
    private var cur2: String? = null
    private var num1: String? = null
    private var num2: String? = null
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.createDataBase(applicationContext)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(receiver, intentFilter)

        initSpinner()
        onDoneClickListener()
        onHistoryClickListener()
        val swapButton = binding.swapButton
        swapButton.setOnClickListener {
            swap()
        }
        progress = binding.progressBar
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString("cur1", cur1)
        savedInstanceState.putString("cur2", cur2)
        savedInstanceState.putString("num1", binding.row1.editTextNumber.text.toString())
        savedInstanceState.putString("num2", binding.row2.editTextNumber.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.let { super.onRestoreInstanceState(it) }
        cur1 = savedInstanceState.getString("cur1")
        cur2 = savedInstanceState.getString("cur2")
        num1 = savedInstanceState.getString("num1")
        num2 = savedInstanceState.getString("num2")
        setParameters()
    }

    private fun onHistoryClickListener() {
        binding.buttonHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun onDoneClickListener() {
        binding.buttonDone.setOnClickListener {
            Utility.hideKeyboard(this)
            val numberToConvert = binding.row1.editTextNumber.text.toString()

            when {
                numberToConvert.isNullOrBlank() || numberToConvert == "0" ->
                    showSnackBar("Empty input", view = binding.ExchangeLayout, context = this)

                !Utility.isNetworkAvailable(this) ->
                    showSnackBar("Internet unavailable", view = binding.ExchangeLayout, context = this)

                else -> runBlocking {
                    launch { doConversion() }
                }
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

    private fun doConversion() {
        progress.visibility = View.VISIBLE
        val apiKey = Links.API_KEY
        val from = cur1.toString()
        val to = cur2.toString()
        val amount = binding.row1.editTextNumber.text.toString().toDouble()
        // do the conversion
        viewModel.getExchangeData(apiKey, from, to, amount)
        viewModel.myResponse.observe(this) { response ->
            if (response.isSuccessful) {
                val ratesMap: Map<String, Rates> = response.body()!!.rates
                if (ratesMap.isNotEmpty()) {
                    val entry = ratesMap.entries.last()
                    val convertedValue = entry.value.rate.toDouble() * amount
                    viewModel.convertedRate.value = convertedValue
                    val finalString = String.format("%.2f", viewModel.convertedRate.value)

                    binding.row2.editTextNumber.setText(finalString)

                    viewModel.performConversionAndSave(
                        from = from,
                        to = to,
                        amount = amount.toString(),
                        convertedValue = finalString
                    )
                    val newEntity = ConversionHistoryEntity(0, from, to, amount.toString(), finalString)
                    viewModel.addEntity(newEntity)
                }
                progress.visibility = View.GONE
            } else {
                showSnackBar("REQUEST ERROR", view = binding.ExchangeLayout, context = this)
            }
        }
    }

    private fun setParameters() {
        binding.row1.textCurrency.text = cur1
        binding.row2.textCurrency.text = cur2
        if (num1 != null) binding.row1.editTextNumber.setText(num1.toString())
        if (num2 != null) binding.row2.editTextNumber.setText(num2.toString())
    }

    private fun swap() {
        cur1 = cur2.also { cur2 = cur1 }
        num1 = binding.row1.editTextNumber.text.toString()
        num2 = binding.row2.editTextNumber.text.toString()
        num1 = num2.also { num2 = num1 }
        setParameters()
    }

    private fun initSpinner() {
        val currencies = CurrenciesAvailable.currenciesList()

        val spinner1 = binding.row1.currenciesSpinner
        val spinner2 = binding.row2.currenciesSpinner
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, currencies)

        spinner1.adapter = arrayAdapter
        spinner2.adapter = arrayAdapter

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.row1.textCurrency.text = currencies[position]
                cur1 = currencies[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.row2.textCurrency.text = currencies[position]
                cur2 = currencies[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}
