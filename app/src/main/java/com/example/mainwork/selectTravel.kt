package com.example.mainwork

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import com.example.mainwork.daos.SavedDataDao
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_select_travel.*
import kotlinx.android.synthetic.main.activity_select_travel.timeButton2
import kotlinx.android.synthetic.main.activity_select_work.*
import java.util.*
import kotlinx.android.synthetic.main.activity_select_travel.pickupDestination as pickupDestination1

class selectTravel : AppCompatActivity() , DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener {

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    private lateinit var saveDataDao : SavedDataDao
    private var bookingId :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_travel)

        timeButton2.setOnClickListener() {
            picData()
        }
        timeText2.setOnClickListener() {
            picData()
        }

        saveDataDao = SavedDataDao()
        var check1 : Boolean = false
        var check2 : Boolean = false
        var check3 : Boolean = false

        switch1.setOnClickListener {

            if (switch1.isChecked) {
                vehicleLayout.visibility = View.VISIBLE

                bikeButton.setOnClickListener {
                    check1 = bikeButton.isClickable
                }
                carButton.setOnClickListener {
                    check2 = carButton.isClickable
                }
                busButton.setOnClickListener {
                    check3 = busButton.isClickable

                }
            } else {
                vehicleLayout.visibility = View.GONE
            }
        }


        nextButton.setOnClickListener() {

            val pickup = pickupDestination.text.toString().trim()
            val destinatonText = destinationTextTravel.text.toString().trim()
            val time = timeText2.text.toString().trim()
            // val amountText : EditText = findViewById(R.id.amountText)

            if (switch1.isChecked) {
                if (check1) {
                    confirmBooking1("1")
                } else if(check2){
                    confirmBooking1("2")
                } else if (check3) {
                    confirmBooking1("3")
                } else {
                    Toast.makeText(this,"Please choose at least one !",Toast.LENGTH_SHORT).show()
                }
            } else {
                confirmBooking1("")
            }

            if (pickup.isEmpty()) {
                findViewById<EditText>(R.id.pickupDestination).error = "Please Enter Destination!"
            } else if (destinatonText.isEmpty()) {
                findViewById<EditText>(R.id.destinationText).error = "Please Enter Destination!"
            } else if (time.isEmpty()) {
                findViewById<EditText>(R.id.timeText2).error = "Please Select TimeZone!"
            } else {
               confirmBooking()
            }

        }

    }

    private fun confirmBooking() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to confirm your booking?")
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id ->
                    changeActivity()
                })
            .setNegativeButton("cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })

        builder.create()
        builder.show()
    }

    private fun confirmBooking1(s: String) {
         bookingId = s
    }

    private fun changeActivity() {
        Toast.makeText(this,"Your Work Successfully Booked!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, CongratulationActivity::class.java)
        startActivity(intent)

        Toast.makeText(this,"aaye kya dekhte hai $bookingId",Toast.LENGTH_LONG).show()
        saveDataDao = SavedDataDao()
        saveDataDao.travelData(pickupDestination.text.toString(),destinationTextTravel.text.toString(),timeText2.text.toString(),descriptionTravel.text.toString(),bookingId)
    }

    private fun getDateTimeCalender() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun picData() {
        getDateTimeCalender()

        DatePickerDialog(this, this ,year,month,day).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalender()

        TimePickerDialog(this,this,hour,minute,true).show()
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        savedHour = hourOfDay
        savedMinute = minute

        timeText2.setText( "$savedDay/$savedMonth/21 $savedHour:$savedMinute")
    }

}