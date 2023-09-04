package com.example.hackout.models

data class Medicine(val name : String, val stock : Int){
    var id : String = ""
    constructor() : this("none",-1)
}
