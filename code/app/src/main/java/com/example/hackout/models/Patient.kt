package com.example.hackout.models

data class Patient(val abhaNumber : String, val name: String, val age : String, val weight : String){
    constructor() : this("none","none","none","none")
}
