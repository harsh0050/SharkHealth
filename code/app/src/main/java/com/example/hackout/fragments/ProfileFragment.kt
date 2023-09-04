package com.example.hackout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hackout.dao.FirestoreDao
import com.example.hackout.databinding.FragmentProfileBinding

private const val USER_ID = "userId"

class ProfileFragment : Fragment() {
    private var userId: String? = null
    private val firestoreDao = FirestoreDao()
    private lateinit var binding: FragmentProfileBinding

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
        binding = FragmentProfileBinding.inflate(inflater)
        binding.progressBar.visibility = View.GONE

        userId?.let{userId->
            binding.progressBar.visibility = View.VISIBLE
            firestoreDao.getProfile(userId).addOnSuccessListener {
                if(it.exists()){
                    binding.userName.text = it.getString("name")
                    binding.hospitalName.text = it.getString("hospitalName")
                    binding.bloodType.text = it.getString("bloodType")
                    binding.contact.text = it.getString("contact")
                }
                binding.progressBar.visibility = View.GONE
            }.addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, it.localizedMessage?.toString() ?: "Error", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_ID, param1)
                }
            }
    }
}