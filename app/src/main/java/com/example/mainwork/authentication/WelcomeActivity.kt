package com.example.mainwork.authentication

import android.content.Intent
import android.graphics.ImageDecoder.ImageInfo
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.mainwork.MainActivity
import com.example.mainwork.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.rilixtech.CountryCodePicker
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity(){

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        auth = Firebase.auth

        if(auth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val ccp = findViewById<CountryCodePicker>(R.id.ccp)
        var ccpCode = ccp.selectedCountryCode

        ccp.setOnCountryChangeListener {
            ccpCode = it.phoneCode
        }

        goButton.setOnClickListener {
            val phoneNumber = inputMobileNo.text.toString().trim()
            val id = "+$ccpCode$phoneNumber"
            var status = ""

            if (phoneNumber.isNotEmpty()) {

                val database = FirebaseDatabase.getInstance().reference
                database.child("User").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        status = if (dataSnapshot.hasChild(id)) {
                            "Welcome Back !!"
                        }else{
                            "Welcome !!"
                        }
                        val intent = Intent (applicationContext, RegisterActivity::class.java)
                        intent.putExtra(RegisterActivity.PHONE_CODE, ccpCode)
                        intent.putExtra(RegisterActivity.PHONE_NO, phoneNumber)
                        intent.putExtra(RegisterActivity.STATUS, status)
                        startActivity(intent)
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            } else {
                Toast.makeText(applicationContext, "Please Enter Mobile No.", Toast.LENGTH_SHORT).show()
            }
        }

    }

}