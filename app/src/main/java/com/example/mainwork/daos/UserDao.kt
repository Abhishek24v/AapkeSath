package com.example.mainwork.daos

import com.example.mainwork.model.User
import com.google.firebase.database.FirebaseDatabase

class UserDao {
    private val db = FirebaseDatabase.getInstance().reference
    private val userCollection = db.child("User")

    fun addUser( user: User) {
        if(user != null){
             userCollection.child(user.phoneNumber).setValue(user)
        }
    }
}