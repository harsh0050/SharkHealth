package com.example.hackout.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hackout.MainActivity
import com.example.hackout.R
import com.example.hackout.SymptomAndPrescriptionActivity
import com.example.hackout.dao.FirestoreDao
import com.example.hackout.databinding.FragmentEditorialPatientBinding
import com.example.hackout.models.Patient

private const val ABHA_NUMBER= "abhaNumber"

class EditorialPatientFragment : Fragment() {
    private var abhaNumber: String? = null
    private lateinit var binding : FragmentEditorialPatientBinding
    private val firestoreDao = FirestoreDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            abhaNumber = it.getString(ABHA_NUMBER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditorialPatientBinding.inflate(inflater)
        abhaNumber?.let { abhaNumber->
            binding.abhaNumberTextView.append(" $abhaNumber")

            //BUG HERE
            firestoreDao.getPatient(abhaNumber).addOnSuccessListener {
                if(it.exists()){
                    binding.patientNameEditText.setText(it.getString("name"))
                    binding.patientAgeEditText.setText(it.getString("age"))
                    binding.patientWeightEditText.setText(it.getString("weight"))
                }
                binding.progressBar.visibility = View.GONE
            }.addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, it.localizedMessage?.toString() ?: "Error", Toast.LENGTH_SHORT).show()
            }

            binding.nextButton.setOnClickListener{
                binding.progressBar.visibility = View.VISIBLE

                val name = binding.patientNameEditText.text.toString()
                val age = binding.patientAgeEditText.text.toString()
                val weight = binding.patientWeightEditText.text.toString()
                val patient = Patient(abhaNumber, name, age, weight)
                firestoreDao.addPatient(patient).addOnSuccessListener {
                    val intent = Intent(context, SymptomAndPrescriptionActivity::class.java)
                    intent.putExtra(MainActivity.ABHA_NUMBER,abhaNumber)
                    intent.putExtra("patientName",name)

                    binding.progressBar.visibility = View.GONE
                    val frag = PatientDetailsFragment.newInstance(abhaNumber,name,age,weight)
                    parentFragmentManager.beginTransaction().replace(R.id.fragment_container_view,frag).commit()
                    startActivity(intent)
                }.addOnFailureListener {

                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, it.localizedMessage?.toString() ?: "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }


        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(abha: String) =
            EditorialPatientFragment().apply {
                arguments = Bundle().apply {
                    putString(ABHA_NUMBER, abha)
                }
            }
    }
}