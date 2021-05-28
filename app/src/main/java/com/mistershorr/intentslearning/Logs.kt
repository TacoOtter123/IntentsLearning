package com.mistershorr.intentslearning

import java.util.*


data class Logs (
    var distance : Double = 0.0,
    var splitTime : Double = 1.0,
    var time : Double = 1.0,
    var name : String = "run",
    var notes : String = "notes",
    var objectId : String? = null,
    var ownerId : String? = null,
    var created : Date = Date()

){

}