package com.example.hackout.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hackout.DisplayMedicineActivity
import com.example.hackout.MainActivity
import com.example.hackout.adapters.CustomEventListener
import com.example.hackout.adapters.PrescriptionAdapter
import com.example.hackout.adapters.PrescriptionViewHolder
import com.example.hackout.dao.FirestoreDao
import com.example.hackout.databinding.FragmentReceivedPrescriptionBinding
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


class ReceivedPrescriptionFragment : Fragment(), CustomEventListener {
    private lateinit var binding : FragmentReceivedPrescriptionBinding
    private val firestoreDao = FirestoreDao()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReceivedPrescriptionBinding.inflate(layoutInflater)

        if(context!=null){
            val pref = requireContext().getSharedPreferences(
                MainActivity.SHARED_PREFERENCE,
                AppCompatActivity.MODE_PRIVATE
            )
            val userId = pref.getString("userId","null")!!

            val adapter = PrescriptionAdapter(this)
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.adapter = adapter
            println("user id:" + userId)

            firestoreDao.getAllPrescriptions(userId).addSnapshotListener(object :
                EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (value != null) {
                        val data = ArrayList<HashMap<String, String>>()
                        value.documents.forEach { doc ->
//                        firestoreDao.getPatient(doc.getString("abhaNumber")!!).addOnSuccessListener {
                            println("data: " + doc.data)
                            data.add(
                                hashMapOf(
                                    "patientName" to doc.getString("patientName")!!,
                                    "abhaNumber" to doc.getString("abhaNumber")!!
                                )
                            )
//                        }
                        }
                        println("size: " + data.size)
                        adapter.updateData(data)

                    }
                }
            })
        }


        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ReceivedPrescriptionFragment()
    }

    override fun onClick(holder: PrescriptionViewHolder) {
        val intent = Intent(requireContext(), DisplayMedicineActivity::class.java)
        intent.putExtra(MainActivity.ABHA_NUMBER,holder.patientAbha.text.toString())
        println("no error yet onClick()")
        startActivity(intent)
    }
}