package com.example.hackout.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.hackout.MainActivity
import com.example.hackout.dao.FirestoreDao
import com.example.hackout.databinding.DialogFragmentAddMedicineBinding
import com.example.hackout.models.Medicine

class UpdateMedicineDialogFragment : DialogFragment() {
    private lateinit var binding: DialogFragmentAddMedicineBinding
    private val firestoreDao = FirestoreDao()
    private var medName: String? = null
    private var stock: String = "0"
    private var medId : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            medId = it.getString(ID_PARAM,"")
            medName = it.getString(NAME_PARAM, "No Value")
            stock = it.getString(STOCK_PARAM, "0")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentAddMedicineBinding.inflate(inflater)
        binding.medicineEditText.setText(medName)
        binding.amountOfMedsEditText.setText(stock)
        binding.addButton.text = "Update"
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
                    firestoreDao.updateMedicine(
                        userId,
                        medId!!,
                        Medicine(name, amt.toInt()).apply {
                            this.id = medId!!
                        }
                    )
                    dismiss()
                } else {
                    Toast.makeText(context, "Fill the fields", Toast.LENGTH_SHORT).show()
                }
            }
            binding.removeButton.setOnClickListener {
                firestoreDao.removeMedicine(userId,medId!!)
                dismiss()
            }
        }


        return binding.root

    }

    companion object {
        fun newInstance(id: String, name: String, stock: String): UpdateMedicineDialogFragment {
            return UpdateMedicineDialogFragment().apply {
                arguments = bundleOf(ID_PARAM to id,NAME_PARAM to name, STOCK_PARAM to stock)
            }
        }

        private const val NAME_PARAM = "name"
        private const val STOCK_PARAM = "stock"
        private const val ID_PARAM = "id"
    }
}