package com.example.hackout

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import com.example.hackout.dao.FirestoreDao
import com.example.hackout.databinding.ActivitySymptomsAndPrescriptionBinding
import com.example.hackout.models.Prescription

class SymptomAndPrescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySymptomsAndPrescriptionBinding
    private val firestoreDao = FirestoreDao()
    private lateinit var userId: String
    private lateinit var pharmacist: String
    private val medicineSuggestion = ArrayList<String>()
    private val medicineIds = ArrayList<String>()
    private val medicineStocks = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySymptomsAndPrescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = getSharedPreferences(MainActivity.SHARED_PREFERENCE, MODE_PRIVATE)
        userId = pref.getString("userId", "null")!!
        pharmacist = pref.getString("pharmacist", "null")!!
        firestoreDao.getAllMedicine(pharmacist).get().addOnSuccessListener {
            it.documents.forEach { doc->
                medicineSuggestion.add(doc.getString("name")!!)
                medicineIds.add(doc.getString("id")!!)
                medicineStocks.add(doc.get("stock",Int::class.java)!!)
            }
        }

        binding.progressBar.visibility = View.GONE

        val abhaNumber = intent.getStringExtra(MainActivity.ABHA_NUMBER)
        val patientName = intent.getStringExtra("patientName")!!

        binding.addButton.setOnClickListener {
            insert()
        }


        binding.submitButton.setOnClickListener {
            var flag = true
            abhaNumber?.let { abhaNumber ->
                binding.progressBar.visibility = View.VISIBLE
                val data = ArrayList<Prescription>()
                for (i in 0 until binding.linearLayout.size - 1) {
                    val view = binding.linearLayout.getChildAt(i)
                    val name = view.findViewById<EditText>(R.id.medicineEditText).text.toString()
                    val dosage = view.findViewById<EditText>(R.id.dosageEditText).text.toString()
                    val amt = view.findViewById<EditText>(R.id.amountOfMedsEditText).text.toString()
                        .toInt()
                    val extraNote =
                        view.findViewById<EditText>(R.id.extraNoteEditText).text.toString()

                    //TODO
                    val idx = medicineSuggestion.indexOf(name)
                    if(idx!=-1){
                        val stock = medicineStocks[idx]
                        println("stock $stock")
                        if(amt>stock){
                            flag = false
                            Toast.makeText(
                                applicationContext,
                                "Insufficient Stocks",
                                Toast.LENGTH_SHORT
                            ).show()
                            break
                        }
                        val pres = Prescription(medicineIds[idx], name, dosage, amt, extraNote)
                        data.add(pres)
                    }else{
                        flag = false
                        Toast.makeText(
                            applicationContext,
                            "Enter Valid Medicine Name",
                            Toast.LENGTH_SHORT
                        ).show()
                        break
                    }
                }

                if(flag){
                    firestoreDao.sendPrescription(
                        pharmacist,
                        patientName,
                        abhaNumber,
                        data
                    )
                    val intent = Intent(applicationContext, HomePageActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                binding.progressBar.visibility = View.GONE

            }
            if (abhaNumber == null) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private var isFormatting = false //for dosage
    private fun insert() {
        val view =
            layoutInflater.inflate(R.layout.prescription_item_view, binding.linearLayout, false)

        val stockTV = view.findViewById<TextView>(R.id.stockTextView)
        val edtxt = view.findViewById<AutoCompleteTextView>(R.id.medicineEditText)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, medicineSuggestion)
        edtxt.setAdapter(adapter)

        edtxt.onFocusChangeListener = object :View.OnFocusChangeListener{
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if(!p1){
                    val med = edtxt.text.toString()
                    val idx = medicineSuggestion.indexOf(med)
                    if(idx!=-1){
                        stockTV.text = "${medicineStocks[idx]} in stock"
                    }else{
                        stockTV.text = "invalid medicine"
                    }
                }
            }
        }
        edtxt.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                stockTV.text = "${medicineStocks[p2]} in stock"
            }
        }

        val dosageEditText = view.findViewById<EditText>(R.id.dosageEditText)

        dosageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) {
                    return
                }

                isFormatting = true
                val formattedText = formatNumber(s.toString())
                dosageEditText.setText(formattedText)
                dosageEditText.setSelection(formattedText.length)
                isFormatting = false
            }
        })

        view.findViewById<ImageView>(R.id.removeButton).setOnClickListener {
            binding.linearLayout.removeView(view)
        }
        binding.linearLayout.addView(view, binding.linearLayout.size - 1)
    }
    private fun formatNumber(input: String): String {
        val digits = input.replace("\\D".toRegex(), "")
        val formattedNumber = StringBuilder()

        var groupIndex = 0
        for (i in 0 until digits.length) {
            formattedNumber.append(digits[i])
            if (i == 0 || i == 1) {
                formattedNumber.append('-')
            }
        }

        return formattedNumber.toString()
    }

}