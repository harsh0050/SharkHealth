package com.example.hackout.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class SubmitDialogueBox(private val callback: CustomCallback): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setPositiveButton("Yes"){dialog, id->
                callback.run()
            }
            setNegativeButton("No"){dialog, id->
                //do nothin'
            }
            setMessage("Submit?")
        }
        return builder.create()


    }
}