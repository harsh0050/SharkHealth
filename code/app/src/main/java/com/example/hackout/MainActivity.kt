package com.example.hackout

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hackout.dao.FirestoreDao
import com.example.hackout.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
//    val db = Firebase.firestore
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    private val firestoreDao = FirestoreDao()

    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
        sharedPreferencesEditor = sharedPreferences.edit()


        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val userId = sharedPreferences.getString("userId", "null")
            userId?.let {
                login(userId)
            }
        }else{
            binding.progressBar.visibility = View.GONE
        }

        binding.loginButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val userId = binding.loginUserIdEditText.text.toString()
            val password = binding.loginPasswordEditText.text.toString()
            if (userId.isNotEmpty() && password.isNotEmpty()) {
                authenticate(userId, password)
            } else {
//                Toast.makeText(applicationContext, "Enter Credentials", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                println("Enter Credentials")
            }
        }
        val imageViewEye = binding.showHidePassword
        val editTextPassword = binding.loginPasswordEditText
        imageViewEye.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                editTextPassword.transformationMethod = null
                imageViewEye.setImageResource(R.drawable.visible)
            } else {
                editTextPassword.transformationMethod = PasswordTransformationMethod()
                imageViewEye.setImageResource(R.drawable.hide)
            }
            editTextPassword.setSelection(editTextPassword.text.length)
        }
    }

    private fun authenticate(userId: String, password: String) {
        GlobalScope.launch(Dispatchers.IO) {

            val doc = firestoreDao.getCredential(userId).await()
            if (doc.exists()) {
                val pass: String? = doc.get("password", String::class.java)
                println(pass)
                if (pass != null && pass == password) {
                    login(userId)
                } else {
                    runOnUiThread{
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(applicationContext, "Invalid Credentials", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                runOnUiThread{
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "Invalid Credentials", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun login(userId: String) {
        GlobalScope.launch {
            val doc= firestoreDao.getCredential(userId).await()
            sharedPreferencesEditor.let {
                it.putBoolean("isLoggedIn", true)
                it.putString("userId", userId)
            }.apply()
            val occupation = doc.get("occupation")!!
//            val occupation ="doctor"
            if(occupation=="doctor"){
                sharedPreferencesEditor.let{
                    it.putString("pharmacist",doc.getString("pharmacist")!!)
                }.apply()

                val intent = Intent(applicationContext, HomePageActivity::class.java)
                startActivity(intent)
                finish()
            }else if(occupation=="pharma"){
                val intent = Intent(applicationContext, HomePagePharmaActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }


    companion object {
        const val CREDENTIAL_DATABASE = "credentialDatabase"
        const val SHARED_PREFERENCE = "sharedPreference"
        const val EDITORIAL_PATIENT = "editorialPatient"
        const val PATIENT_DETAILS = "patientDetails"
        const val ABHA_NUMBER = "abhaNumber"
    }
}