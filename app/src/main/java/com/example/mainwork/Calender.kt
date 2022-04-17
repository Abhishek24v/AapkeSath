package com.example.mainwork

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import java.util.*

class Calender : DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener  {

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

    lateinit var work : SelectWork
    //private val workTime : String()

    private fun getDateTimeCalender() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

     fun picData() : String {
        getDateTimeCalender()

        DatePickerDialog(work, this ,year,month,day).show()
         return  "$savedDay/$savedMonth/$savedYear\n Hours:$savedHour:$savedMinute"
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalender()

        TimePickerDialog(work,this,hour,minute,true).show()
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        savedHour = hourOfDay
        savedMinute = minute
      /*  work = WorkConfirmation()
        val valueT = work.timeText
        valueT.setText( "$savedDay/$savedMonth/$savedYear\n Hours:$savedHour:$savedMinute")*/
    }

}