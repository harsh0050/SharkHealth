package com.example.hackout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hackout.R
import com.example.hackout.databinding.FragmentPatientDetailsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ABHA_NUMBER = "abhaNumber"
private const val NAME = "name"
private const val AGE = "age"
private const val WEIGHT = "weight"

class PatientDetailsFragment : Fragment() {
    private var abhaNumber: String? = null
    private var name: String? = null
    private var age: String? = null
    private var weight: String? = null
    private lateinit var binding : FragmentPatientDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            abhaNumber = it.getString(ABHA_NUMBER)
            name = it.getString(NAME)
            age = it.getString(AGE)
            weight = it.getString(WEIGHT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatientDetailsBinding.inflate(inflater)
        binding.abhaNumberTextView.append(abhaNumber)
        binding.patientNameTextView.text = name
        binding.patientAgeTextView.text = age
        binding.patientWeightTextView.text = weight
        binding.nextButton.setOnClickListener{
            abhaNumber?.let{
                val frag = EditorialPatientFragment.newInstance(it)
                parentFragmentManager.beginTransaction().replace(R.id.fragment_container_view,frag).commit()
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(abha: String, name: String, age: String, weight: String) =
            PatientDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ABHA_NUMBER, abha)
                    putString(NAME, name)
                    putString(AGE, age)
                    putString(WEIGHT, weight)
                }
            }
    }
}