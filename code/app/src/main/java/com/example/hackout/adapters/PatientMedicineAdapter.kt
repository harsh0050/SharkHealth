package com.example.hackout.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hackout.R
import com.example.hackout.models.Prescription

class PatientMedicineAdapter(private val data : ArrayList<Prescription>) : RecyclerView.Adapter<PatientMedicineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientMedicineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medicine_item_view,parent,false)
        return PatientMedicineViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PatientMedicineViewHolder, position: Int) {
        val curr = data[position]
        holder.name.text = curr.name
        holder.dosage.append(curr.dosage)
        holder.amount.append(curr.quantity.toString())
        if(curr.extraNote.isNotEmpty()){
            holder.extraNote.append(curr.extraNote)
        }else{
            holder.extraNote.visibility = View.GONE
        }

    }
}
class PatientMedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val name : TextView = itemView.findViewById(R.id.medicineTextView)
    val dosage : TextView = itemView.findViewById(R.id.dosageTextView)
    val amount : TextView = itemView.findViewById(R.id.amountOfMedsTextView)
    val extraNote : TextView = itemView.findViewById(R.id.extraNoteTextView)
}
