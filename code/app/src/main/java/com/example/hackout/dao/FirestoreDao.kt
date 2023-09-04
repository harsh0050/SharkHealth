package com.example.hackout.dao

import com.example.hackout.MainActivity
import com.example.hackout.models.Medicine
import com.example.hackout.models.Patient
import com.example.hackout.models.Prescription
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreDao {
    private val db = Firebase.firestore
    fun getCredential(userId: String): Task<DocumentSnapshot> {
        return db.collection(MainActivity.CREDENTIAL_DATABASE).document(userId).get()
    }

    fun getProfile(userId: String): Task<DocumentSnapshot> {
        return db.collection(PROFILE_DATABASE).document(userId).get()
    }

    fun addPatient(patient: Patient): Task<Void> {
        return db.collection(PATIENT_DATABASE).document(patient.abhaNumber).set(patient)
    }

    fun getPatient(abhaNumber: String): Task<DocumentSnapshot> {
        return db.collection(PATIENT_DATABASE).document(abhaNumber).get()
    }

    fun sendPrescription(
        pharmacist: String,
        patientName: String,
        abhaNumber: String,
        prescription: ArrayList<Prescription>
    ) {

        db.collection(PHARMACY_INBOX).document(pharmacist).collection("inbox").document(abhaNumber)
            .set(
                hashMapOf(
                    "patientName" to patientName,
                    "abhaNumber" to abhaNumber,
                    "time" to System.currentTimeMillis()
                )
            )
        val ref = db.collection(PHARMACY_INBOX).document(pharmacist).collection("inbox")
            .document(abhaNumber).collection("prescription")
        prescription.forEach {
            ref.add(it)
        }
    }

    fun getAllPrescriptions(pharmacist: String): Query {
        return db.collection(PHARMACY_INBOX).document(pharmacist).collection("inbox")
            .orderBy("time")
    }

    fun getPrescription(pharmacist: String, abhaNumber: String): Task<QuerySnapshot> {
        return db.collection(PHARMACY_INBOX).document(pharmacist).collection("inbox")
            .document(abhaNumber).collection("prescription").get()
    }

    fun deletePrescription(pharmacist: String, abhaNumber: String) {
        val ref = db.collection(PHARMACY_INBOX).document(pharmacist).collection("inbox")
            .document(abhaNumber)
        ref.delete()
        ref.collection("prescription").get().addOnSuccessListener {
            it.documents.forEach { doc->
                doc.reference.delete()
            }
        }
    }

    fun addMedicine(pharmacist: String, medicine: Medicine): Task<Void> {
        val id = System.currentTimeMillis().toString()
        medicine.id = id
        return db.collection("$MEDICINE_DATABASE/$pharmacist/stock").document(medicine.id)
            .set(medicine)
    }

    fun getAllMedicine(pharmacist: String): Query {
        return db.collection("$MEDICINE_DATABASE/$pharmacist/stock").orderBy("name")
    }

    fun updateMedicine(pharmacist: String, medId: String, medicine: Medicine) {
        removeMedicine(pharmacist, medId)
        addMedicine(pharmacist, medicine)
    }

    fun updateStocks(pharmacist: String, medicineId: String, amountLeft: Int) {
        db.collection(MEDICINE_DATABASE).document(pharmacist).collection("stock")
            .document(medicineId).update(
                "stock", amountLeft
            )
    }

    fun getMedicine(pharmacist: String, medicineId: String): Task<DocumentSnapshot> {
        return db.collection(MEDICINE_DATABASE).document(pharmacist).collection("stock")
            .document(medicineId).get()
    }


     fun removeMedicine(pharmacist: String, medId: String): Task<Void> {
        return db.collection("$MEDICINE_DATABASE/$pharmacist/stock").document(medId).delete()
    }


    companion object {
        const val PATIENT_DATABASE = "patientDatabase"
        const val PHARMACY_INBOX = "pharmacyInboxDatabase"
        const val PROFILE_DATABASE = "profileDatabase"
        const val MEDICINE_DATABASE = "medicineDatabase"
    }

}