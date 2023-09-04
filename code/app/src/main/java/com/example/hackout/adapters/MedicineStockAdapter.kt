package com.example.hackout.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hackout.R
import com.example.hackout.models.Medicine

class MedicineStockAdapter(private val listener: MedicineClickListener) : RecyclerView.Adapter<MedicineStockViewHolder>() {
    private var data : ArrayList<Medicine> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineStockViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stock_medicine_item_view,parent,false)
        val holder = MedicineStockViewHolder(view)
        view.setOnClickListener {
            listener.onClick(holder)
        }
        return holder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MedicineStockViewHolder, position: Int) {
        val curr = data[position]
        holder.name.text = curr.name
        holder.stock.text = "Quantity: " + (curr.stock.toString())
        holder.id = curr.id
    }

    fun updateData(newData : ArrayList<Medicine>){
        this.data = newData
        notifyDataSetChanged()
    }
}

interface MedicineClickListener {
    fun onClick(holder: MedicineStockViewHolder)
}

class MedicineStockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val name : TextView = itemView.findViewById(R.id.medicineTextView)
    val stock : TextView = itemView.findViewById(R.id.amountOfMedsTextView)
    var id : String =""
}