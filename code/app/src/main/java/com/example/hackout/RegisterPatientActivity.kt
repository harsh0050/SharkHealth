package com.example.hackout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import com.example.hackout.dao.FirestoreDao
import com.example.hackout.databinding.ActivityRegisterPatientBinding
import com.example.hackout.fragments.EditorialPatientFragment
import com.example.hackout.fragments.PatientDetailsFragment

class RegisterPatientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterPatientBinding
    private val firestoreDao = FirestoreDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manager = supportFragmentManager.beginTransaction()
        val abhaNumber = intent.getStringExtra(MainActivity.ABHA_NUMBER)!!
        firestoreDao.getPatient(abhaNumber).addOnSuccessListener {
            if (it.exists()) {
                val frag = PatientDetailsFragment.newInstance(
                    abhaNumber,
                    it.getString("name")!!,
                    it.getString("age")!!,
                    it.getString("weight")!!
                )
                manager.replace(R.id.fragment_container_view,frag)
            }else{
                val frag = EditorialPatientFragment.newInstance(abhaNumber)
                manager.replace(R.id.fragment_container_view,frag)
            }
            manager.commit()

        }


    }

}