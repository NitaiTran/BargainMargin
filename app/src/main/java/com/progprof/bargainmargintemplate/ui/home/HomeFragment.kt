package com.progprof.bargainmargintemplate.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.progprof.bargainmargintemplate.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var currentValue = 0.0 //Holds current num that will be displayed, JMoraHi


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshUI() // show starting value on screen (starts at 0), JMoraHi

        fun readDeltaOrNull(): Double? = binding.inputDelta.text.toString().trim().toDoubleOrNull() // helper: read the number user typed; returns null if empty/invalid, JMoraHi

        // ADD button pressed
        binding.addButton.setOnClickListener {
            val delta = readDeltaOrNull() // get amount to add, JMoraHi
            if(delta == null) {
                binding.inputDelta.error = "Enter a number" // ask user for a number, JMoraHi
                return@setOnClickListener // stop if no valid input, JMoraHi
            }

            currentValue += delta // add to running total, JMoraHi
            refreshUI() // update the label, JMoraHi
        }

        // SUBTRACT button pressed
        binding.subtractButton.setOnClickListener {
            val delta = readDeltaOrNull() // get amount to subtract, JMoraHi
            if(delta == null) {
                binding.inputDelta.error = "Enter a number" // ask user for a number, JMoraHi
                return@setOnClickListener // stop if no valid input, JMoraHi
            }

            currentValue -= delta //subtract from running total, JMoraHi
            refreshUI()
        }


    }

    //update the text view with currentValue (no '.0' for whole numbers), JMoraHi
    private fun refreshUI(){
        val asInt = currentValue.toInt()
        val text = if(currentValue == asInt.toDouble()) asInt.toString() else currentValue.toString()
        binding.resultText.text = "Result: $text"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}