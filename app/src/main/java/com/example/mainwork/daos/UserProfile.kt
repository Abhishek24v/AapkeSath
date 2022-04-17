package com.example.mainwork.daos

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.mainwork.R
import com.example.mainwork.authentication.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_user_profile.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

class UserProfile : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        auth = Firebase.auth
        val currentUserPhoneNo = auth.currentUser?.phoneNumber.toString()
        val dbRef = FirebaseDatabase.getInstance().reference.child("User/$currentUserPhoneNo")
        dbRef.child("userName").get().addOnSuccessListener {
            userName.text = it.value.toString()
        }.addOnFailureListener {
        }

        dbRef.child("userGender").get().addOnSuccessListener {
            userName.text = it.value.toString()
        }.addOnFailureListener {
        }

        /*
               userId.text = currentUser?.email

               if(currentUser?.photoUrl?.toString() != null ) {
                   Glide.with(applicationContext).load(currentUser.photoUrl.toString()).circleCrop().into(userImage)
               }

               signOutButton.setOnClickListener {
                   openSignOutDialog()
               }*/
    }

   /* private fun openSignOutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.exit)
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog , _ ->
                    signoutfun()
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                })
            .setNegativeButton(
               "No",
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })
        builder.create()
        builder.show()
    }

    private fun signoutfun() {
        FirebaseAuth.getInstance().signOut()
    }*/
}
