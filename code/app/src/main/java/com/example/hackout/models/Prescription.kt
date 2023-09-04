package com.example.hackout.models

data class Prescription(
    val medicineId: String,
    val name : String,
    val dosage: String,
    val quantity: Int,
    val extraNote: String
) {
    constructor(medicineId: String,name: String, dosage: String, quantity: Int) : this(medicineId,"", dosage, quantity, "")
    constructor() : this("none","none","none",-1,"none")
}
