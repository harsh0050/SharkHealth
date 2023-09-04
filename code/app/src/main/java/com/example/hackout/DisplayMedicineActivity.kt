package com.example.hackout

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hackout.adapters.PatientMedicineAdapter
import com.example.hackout.dao.FirestoreDao
import com.example.hackout.databinding.ActivityDisplayMedicineBinding
import com.example.hackout.fragments.CustomCallback
import com.example.hackout.fragments.SubmitDialogueBox
import com.example.hackout.models.Prescription
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DisplayMedicineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayMedicineBinding
    private val firestoreDao = FirestoreDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = getSharedPreferences(MainActivity.SHARED_PREFERENCE, MODE_PRIVATE)
        val userId = pref.getString("userId", "null")!!


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        var adapter = PatientMedicineAdapter(ArrayList())
        binding.recyclerView.adapter = adapter
        val abhaNumber = intent.getStringExtra(MainActivity.ABHA_NUMBER)!!

        val newData = ArrayList<Prescription>()
        firestoreDao.getPrescription(userId, abhaNumber).addOnSuccessListener {
            val data = it.documents
            data.forEach { data ->
                newData.add(data.toObject(Prescription::class.java)!!)
            }

            adapter = PatientMedicineAdapter(newData)
            binding.recyclerView.adapter = adapter
        }.addOnFailureListener {
            Toast.makeText(
                applicationContext,
                it.localizedMessage?.toString() ?: "Error",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.submitButton.setOnClickListener {
            SubmitDialogueBox(object : CustomCallback{
                override fun run() {
                    firestoreDao.deletePrescription(userId, abhaNumber)

                    GlobalScope.launch(Dispatchers.IO) {
                        newData.forEach {
                            val doc= firestoreDao.getMedicine(userId, it.medicineId).await()
                            val left = doc.get("stock", Int::class.java)!! - it.quantity
                            firestoreDao.updateStocks(userId,it.medicineId,left)
                        }
                    }
                    finish()
                }

            }).show(supportFragmentManager.beginTransaction(),"submitTag")
        }


    }
}