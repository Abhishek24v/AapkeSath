package com.example.mainwork.daos

import com.google.firebase.database.FirebaseDatabase

class SavedDataDao  {

    val db = FirebaseDatabase.getInstance()
    val collection = db.reference.child("WorkInfo").child("Booking Request").push()
    val collectionTravel = db.reference.child("TravelInfo").child("Booking Request").push()

    fun workData(text1 : String, text : String,description:String,workType:String) {

        when (workType) {
            "1" -> {
                collection.child("Work Type").setValue("Shopping Request")
            }
            "2" -> {
                collection.child("Work Type").setValue("Medicose Request")
            }
            "3" -> {
                collection.child("Work Type").setValue("Doctor Appointment Request")
            }
        }

        collection.child("Description").setValue(description)
        collection.child("Destination").setValue(text1)
        collection.child("Time").setValue(text)

    }

    fun travelData(pickup:String,destination:String,time:String,description: String,vehicleType: String) {

        when (vehicleType) {
            "1" -> {
                collectionTravel.child("Vehicle Type").setValue("2-wheller booking Request")
            }
            "2" -> {
                collectionTravel.child("Vehicle Type").setValue("4-wheller booking Request")
            }
            "3" -> {
                collectionTravel.child("Vehicle Type").setValue("Ambulance booking Request")
            }
        }

        collectionTravel.child("Pickup").setValue(pickup)
        collectionTravel.child("Destination").setValue(destination)
        collectionTravel.child("Time").setValue(time)
        collectionTravel.child("Description").setValue(description)

        }


}