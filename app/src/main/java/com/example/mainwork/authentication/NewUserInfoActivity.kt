package com.example.mainwork.authentication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.mainwork.MainActivity
import com.example.mainwork.R
import com.example.mainwork.daos.UserDao
import com.example.mainwork.model.User
import kotlinx.android.synthetic.main.activity_new_user_info.*
import java.time.LocalDate
import java.time.Period
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class NewUserInfoActivity : AppCompatActivity() {

    var genderPicker: RadioGroup? = null
    lateinit var radioButton: RadioButton

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user_info)

        val datePicker = findViewById<DatePicker>(R.id.datePicker)

        genderPicker = findViewById(R.id.genderSelector)

        var userDob = ""

        dateOfBirth.setOnClickListener {
            if (datePicker.visibility == View.GONE){
                datePicker.visibility = View.VISIBLE
            }else{
                datePicker.visibility = View.GONE
            }
        }

        val today = Calendar.getInstance()
        var userAge = 0

        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        { _, year, month, day ->
            val month = month + 1
            userDob = "$day/$month/$year"
            val msg = "Date Of Birth - $userDob"
            dobText.text = msg
            userAge = getAge(year,month,day)
        }

        doneButton.setOnClickListener {
            if (genderPicker!!.checkedRadioButtonId == -1)
            {
                Toast.makeText(applicationContext, "Please select your gender", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val selectedOption= genderPicker!!.checkedRadioButtonId
                radioButton = findViewById(selectedOption)
            }

            val userGender = radioButton.text.toString()
            val userName = userNameText.text.toString()
            val userEmail = userEmailText.text.toString()
            val userPhoneNo = intent.getStringExtra("phoneNo").toString()

            if (isValidUsername(userName)){
                userNameText.error = "Please enter a valid User Name!"
            }else if(dobText.text.isNullOrEmpty()){
                dobText.error = "Please select you date of birth !"
            }
            else{
                addUserData(userPhoneNo,userName,userDob,userAge.toString(),userGender,userEmail)
            }
        }

        }

    private fun isValidUsername(name: String?): Boolean {

        val regex = "^[A-Za-z]\\w{5,29}$"
        val p: Pattern = Pattern.compile(regex)

        if (name == null) {
            return false
        }
        val m: Matcher = p.matcher(name)

        return m.matches()
    }

    private fun addUserData(userPhoneNo: String, userName: String, userDob: String, userAge: String, userGender: String, userEmail: String) {

        val user = User(userPhoneNo,userName,userDob,userAge,userGender,userEmail)
        val userDao = UserDao()
        userDao.addUser(user)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAge(year: Int, month: Int, dayOfMonth: Int): Int {
        return Period.between(
            LocalDate.of(year, month, dayOfMonth),
            LocalDate.now()
        ).years
    }

}