package com.example.hackout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hackout.adapters.MedicineClickListener
import com.example.hackout.adapters.MedicineStockAdapter
import com.example.hackout.adapters.MedicineStockViewHolder
import com.example.hackout.dao.FirestoreDao
import com.example.hackout.databinding.FragmentMedicineStockBinding
import com.example.hackout.models.Medicine

private const val USER_ID = "userId"

class MedicineStockFragment : Fragment(), MedicineClickListener {
    private var userId: String? = null
    private lateinit var binding : FragmentMedicineStockBinding
    private val firestoreDao = FirestoreDao()

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
        binding = FragmentMedicineStockBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = MedicineStockAdapter(this)
        binding.recyclerView.adapter = adapter
        firestoreDao.getAllMedicine(userId!!).addSnapshotListener{querySnapshot, error->
            val data = ArrayList<Medicine>()
            querySnapshot?.documents?.forEach {doc->
                val nTemp = doc.getString("name")!!
                val sTemp = doc.get("stock",Int::class.java)!!
                data.add(Medicine(nTemp,sTemp).apply { this.id=doc.getString("id")!! })
            }
            adapter.updateData(data)
        }
        binding.addMedicineButton.setOnClickListener {
            val manager = childFragmentManager.beginTransaction()
            val dialog = AddMedicineDialogFragment()
            dialog.show(manager, DIALOG_TAG)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(userId: String) =
            MedicineStockFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_ID, userId)
                }
            }
        const val DIALOG_TAG = "dialogTag"
    }

    override fun onClick(holder: MedicineStockViewHolder) {
        val medName = holder.name.text.toString()
        val stock = holder.stock.text.toString().split(" ")[1]
        val id = holder.id
        val dialog = UpdateMedicineDialogFragment.newInstance(id,medName,stock)
        dialog.show(childFragmentManager, DIALOG_TAG)
    }
}