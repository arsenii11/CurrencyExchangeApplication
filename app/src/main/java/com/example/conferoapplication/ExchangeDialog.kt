package com.example.conferoapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.example.conferoapplication.Utilities.Links
import com.example.conferoapplication.Utilities.Resource
import com.example.conferoapplication.Utilities.Utility
import com.example.conferoapplication.databinding.FragmentExchangeBinding
import com.example.conferoapplication.model.Rates
import com.example.conferoapplication.vm.ExchangeVM
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*

@AndroidEntryPoint
class ExchangeDialog : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentExchangeBinding
    private val vm: ExchangeVM by viewModels()
    private var cur1: String? = null
    private var cur2: String? = null
    private lateinit var progress: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentExchangeBinding.inflate(inflater, container, false)

        return binding.root
    }



    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()


        binding.buttonDone.setOnClickListener {

            activity?.let { Utility.hideKeyboard(it) }

            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.show()

            view.delayOnLifecycle(2000L){
                dialog.dismiss()
            }



            val numberToConvert = binding.row1.editTextNumber.text.toString()

            if (numberToConvert.isEmpty() || numberToConvert == "0") {
                showSnackbar("Empty input")
            }

            //check if internet is available
            else if (!Utility.isNetworkAvailable(activity)) {
                showSnackbar("Internet unavailable")
            }

            //carry on and convert the value
            else {
                doConversion()
            }
        }


        val SwapBt = binding.swapButton
        SwapBt.setOnClickListener {
            Swap()
        }

        progress = binding.progressBar

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
    private fun showSnackbar(text: String){
        Snackbar.make(binding.ExchangeLayout,text, Snackbar.LENGTH_LONG)
            .setTextColor(ContextCompat.getColor(activity!!, R.color.white))
            .background(ContextCompat.getColor(activity!!, R.color.red))
            .show()
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
        val currenc = arrayOf("EUR", "USD", "RUB", "SEK","ANG","BYN","COP","PLN")

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

                            val result = map[it]?.rate_for_amount

                            vm.convertedRate.value = result

                            Locale.setDefault(Locale.ROOT);
                            val finalString = String.format("%,.2f", vm.convertedRate.value)


                            binding.row2.editTextNumber.setText(finalString)

                        }
                        progress.visibility = View.GONE

                    }
                    else if (result.data?.status == "fail") {

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



/*    private fun addProgressBar(): ProgressBar {
        // Create progressBar dynamically...
        val progressBar = ProgressBar(activity)
        progressBar.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        progressBar.visibility = View.INVISIBLE

        // Add ProgressBar to LinearLayout
        binding.ExchangeLayout.addView(progressBar)


        progressBar.id = View.generateViewId()



        return progressBar
    }*/


}



