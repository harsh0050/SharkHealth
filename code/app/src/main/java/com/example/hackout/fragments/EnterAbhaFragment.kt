package com.example.hackout.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hackout.MainActivity
import com.example.hackout.RegisterPatientActivity
import com.example.hackout.databinding.FragmentEnterAbhaBinding

private const val USER_ID = "userId"

class EnterAbhaFragment : Fragment() {
    private var userId: String? = null
    private lateinit var binding : FragmentEnterAbhaBinding
    private var isFormatting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterAbhaBinding.inflate(inflater)
        binding.progressBar.visibility = View.GONE

        binding.abhaNumberEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) {
                    return
                }

                isFormatting = true
                val formattedText = formatNumber(s.toString())
                binding.abhaNumberEditText.setText(formattedText)
                binding.abhaNumberEditText.setSelection(formattedText.length)
                isFormatting = false
            }
        })

        binding.registerPatientButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val abhaNumber = binding.abhaNumberEditText.text.toString()
            if(abhaNumber.isNotEmpty()){
                val intent = Intent(context,RegisterPatientActivity::class.java)
                intent.putExtra(MainActivity.ABHA_NUMBER,abhaNumber)
                binding.progressBar.visibility = View.GONE
                startActivity(intent)
            }else{
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Enter ABHA Number", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            EnterAbhaFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_ID, param1)
                }
            }
    }
    private fun formatNumber(input: String): String {
        val digits = input.replace("\\D".toRegex(), "")
        val formattedNumber = StringBuilder()

        var groupIndex = 0
        for (i in 0 until digits.length) {
            formattedNumber.append(digits[i])
            if (i == 1 || i == 5 || i == 9) {
                formattedNumber.append('-')
            }
        }

        return formattedNumber.toString()
    }
}