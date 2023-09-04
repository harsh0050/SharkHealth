package com.example.hackout.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hackout.R

class PrescriptionAdapter(private val listener: CustomEventListener) : RecyclerView.Adapter<PrescriptionViewHolder>() {

    private var data : ArrayList<HashMap<String, String>> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrescriptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.patient_item_view,parent,false)
        val holder = PrescriptionViewHolder(view)
        view.setOnClickListener {
            listener.onClick(holder)
        }
        return holder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PrescriptionViewHolder, position: Int) {
        println("onBindViewHolder()")
        val name : String = data[position]["patientName"]!!
        val abha : String = data[position]["abhaNumber"]!!
        holder.patientName.text = name
        holder.patientAbha.text = abha
    }

    fun updateData(newData : ArrayList<HashMap<String, String>>){
        this.data = newData
        notifyDataSetChanged()
    }


}

interface CustomEventListener {
    fun onClick(holder : PrescriptionViewHolder)

}

class PrescriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var patientName : TextView = itemView.findViewById(R.id.patientNameTextView)
    var patientAbha : TextView = itemView.findViewById(R.id.abhaNumberTextView)
}