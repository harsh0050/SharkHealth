package com.example.hackout

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hackout.adapters.HomePageViewPagerAdapter
import com.example.hackout.databinding.ActivityHomePageBinding
import com.example.hackout.fragments.CustomCallback
import com.example.hackout.fragments.LogoutDialog
import com.google.android.material.tabs.TabLayoutMediator

class HomePageActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomePageBinding
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences =
            getSharedPreferences(MainActivity.SHARED_PREFERENCE, Context.MODE_PRIVATE)

        val userId = sharedPreferences.getString("userId", "null")

        val adapter = userId?.let { HomePageViewPagerAdapter(it, this) }
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->

            when (position) {
                0 ->
                    tab.text = "Profile"

                else ->
                    tab.text = "Register Patient"

            }

        }.attach()

        binding.logoutButton.setOnClickListener {
            LogoutDialog(object : CustomCallback{
                override fun run() {
                    finish()
                }
            }).show(supportFragmentManager.beginTransaction(),"logoutTag")
        }


    }

    fun checkNetworkConnectivity(): Boolean {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = manager.activeNetwork ?: return false
        val cap = manager.getNetworkCapabilities(network) ?: return false
        return cap.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }

}