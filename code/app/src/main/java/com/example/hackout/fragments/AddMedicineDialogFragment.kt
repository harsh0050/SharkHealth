package com.example.hackout.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.hackout.MainActivity
import com.example.hackout.dao.FirestoreDao
import com.example.hackout.databinding.DialogFragmentAddMedicineBinding
import com.example.hackout.models.Medicine

class AddMedicineDialogFragment : DialogFragment() {
    private lateinit var binding: DialogFragmentAddMedicineBinding
    private val firestoreDao = FirestoreDao()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentAddMedicineBinding.inflate(inflater)
        binding.removeButton.visibility = View.GONE
        if (context != null) {
            val pref = requireContext().getSharedPreferences(
                MainActivity.SHARED_PREFERENCE,
                Context.MODE_PRIVATE
            )
            val userId = pref.getString("userId", "null")!!
            binding.addButton.setOnClickListener {
                val name = binding.medicineEditText.text.toString().trim()
                val amt = binding.amountOfMedsEditText.text.toString()
                if (name.isNotEmpty() && amt.isNotEmpty()) {
                    firestoreDao.addMedicine(
                        pharmacist = userId,
                        medicine = Medicine(name, amt.toInt())
                    )
                    dismiss()
                } else {
                    Toast.makeText(context, "Fill the fields", Toast.LENGTH_SHORT).show()
                }
            }
        }


        return binding.root

    }

}