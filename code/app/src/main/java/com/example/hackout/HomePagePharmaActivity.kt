package com.example.hackout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hackout.adapters.HomePagePharmaViewPagerAdapter
import com.example.hackout.dao.FirestoreDao
import com.example.hackout.databinding.ActivityHomePagePharmaBinding
import com.example.hackout.fragments.CustomCallback
import com.example.hackout.fragments.LogoutDialog
import com.google.android.material.tabs.TabLayoutMediator

class HomePagePharmaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomePagePharmaBinding
    private val firestoreDao = FirestoreDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePagePharmaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = getSharedPreferences(MainActivity.SHARED_PREFERENCE, MODE_PRIVATE)
        val userId = pref.getString("userId","null")!!
        binding.viewPager.adapter = HomePagePharmaViewPagerAdapter(userId, this)
        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab, position ->
            when (position) {
                0 ->
                    tab.text = "Prescriptions"

                1 ->
                    tab.text = "Medicine Stock"

                else ->
                    tab.text = "Prescriptions"
            }
            
        }.attach()

        binding.logoutButton.setOnClickListener {
            LogoutDialog(object : CustomCallback {
                override fun run() {
                    finish()
                }
            }).show(supportFragmentManager.beginTransaction(),"logoutTag")
        }


    }
}