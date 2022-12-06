package com.example.conferoapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.conferoapplication.databinding.FragmentExchangeBinding
import com.example.conferoapplication.vm.ExchangeVM
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExchangeDialog: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentExchangeBinding
    private val vm: ExchangeVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentExchangeBinding.inflate(inflater, container, false)

        return binding.root
    }

    //test array
    val currenc= arrayOf("EUR","USD", "RUB", "SEK")



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner_1= binding.row1.currenciesSpinner
        val spinner_2= binding.row2.currenciesSpinner
        val arrayAdapter = ArrayAdapter<String>(requireActivity(),android.R.layout.simple_spinner_dropdown_item,currenc)

        val doneButton = binding.buttonSecond

        spinner_1.adapter = arrayAdapter
        spinner_2.adapter = arrayAdapter



        vm.loading.bind {

            doneButton.setOnClickListener {

            }


            spinner_1.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val cur_text =  binding.row1.textCurrency
                    cur_text.text = currenc[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }

            spinner_2.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val cur_text =  binding.row2.textCurrency
                    cur_text.text = currenc[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }
    }

    private fun <T> Flow<T>.bind(onChange: (T) -> Unit) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                this@bind.collectLatest(onChange)
            }
        }
    }



}