package com.example.hackout.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.hackout.MainActivity

class LogoutDialog(private val callback : CustomCallback) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context).apply {
            this.setNegativeButton("No", object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
                    //do nothing
                }
            })
            this.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
                    context.getSharedPreferences(
                        MainActivity.SHARED_PREFERENCE,
                        Context.MODE_PRIVATE
                    ).edit().clear().apply()
                    startActivity(Intent(context,MainActivity::class.java))
                    callback.run()
                }
            })
            this.setMessage("Do you want to logout?")
        }

        return builder.create()
    }
}

interface CustomCallback {
    fun run()
}
