package com.example.mainwork.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mainwork.MainActivity
import com.example.mainwork.R
import com.example.mainwork.daos.UserDao
import com.example.mainwork.model.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit

class RegisterActivity : AppCompatActivity() {

    companion object {
        const val PHONE_NO = "phoneNumber"
        const val PHONE_CODE = "ccpCode"
        const val STATUS = "status"
    }

    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                val code  = credential.smsCode
                if (code!=null) {
                    enterOtp.setText(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext,"Auth Fail.", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                storedVerificationId = verificationId
                resendToken = token
            }
        }

        val userStatus = intent.getStringExtra(STATUS)!!.toString()
        userStatusText.text = userStatus
        val phoneNumber = intent.getStringExtra(PHONE_NO)!!
        val phoneCode  = intent.getStringExtra(PHONE_CODE)!!.toString()
        val userId = "+$phoneCode$phoneNumber"

        startPhoneVerification(userId)

        submitOtp.setOnClickListener {
            val otp = enterOtp.text.toString().trim()
            when {
                otp.isNotEmpty() -> {
                    verifyPhoneNumberWithCode(otp)
                }
                otp.length!=6 -> {
                    Toast.makeText(applicationContext,"Invalid OTP", Toast.LENGTH_SHORT).show()
                }
                else -> {

                    Toast.makeText(applicationContext,"Blank Field can not be Proceed.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startPhoneVerification(phoneNumber:String) {

        Toast.makeText(applicationContext,"OTP sent", Toast.LENGTH_SHORT).show()

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyPhoneNumberWithCode(code: String) {

        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signUp(credential)
    }

    private fun signUp(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = task.result?.user
                    val userStatus = intent.getStringExtra(STATUS)!!.toString()
                    if(userStatus == "Welcome !!"){

                        Toast.makeText(applicationContext,"SignUp Successfully", Toast.LENGTH_LONG).show()

                        val phoneNumber = intent.getStringExtra(PHONE_NO)!!
                        val phoneCode  = intent.getStringExtra(PHONE_CODE)!!.toString()
                        val userId = "+$phoneCode$phoneNumber"

                        val mainIntent = Intent(applicationContext, NewUserInfoActivity::class.java)
                        mainIntent.putExtra("phoneNo",userId)
                        startActivity(mainIntent)

                    }else if (userStatus == "Welcome Back !!"){

                        val mainIntent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(mainIntent)
                    }

                } else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {

                        Toast.makeText(applicationContext,"Code Enter is Incorrect!", Toast.LENGTH_LONG).show()
                        enterOtp.setText("")
                    }

                }
            }
    }

}