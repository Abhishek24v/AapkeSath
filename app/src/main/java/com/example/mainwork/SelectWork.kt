package com.example.mainwork

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentActivity
import com.example.mainwork.daos.SavedDataDao
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_select_work.*
import org.json.JSONObject
import java.util.*


class SelectWork : AppCompatActivity() , PaymentResultListener  {

    private lateinit var saveDataDao : SavedDataDao
    private var selectedWorkType: String = ""
    private var format = ""
    private var TAG = "Tag"

    companion object {

        const val NAME_EXTRA = "name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_work)
        Checkout.preload(getApplicationContext())

        val datePicker = findViewById<DatePicker>(R.id.datePickerWork)
        val timePicker = findViewById<TimePicker>(R.id.timePickerWork)

        selectDate.setOnClickListener {
            if (datePicker.visibility == View.GONE){
                datePicker.visibility = View.VISIBLE
            }else{
                datePicker.visibility = View.GONE
            }
        }
        selectTime.setOnClickListener {
            if (timePicker.visibility == View.GONE){
                timePicker.visibility = View.VISIBLE
            }else{
                timePicker.visibility = View.GONE
            }
        }

        val today = Calendar.getInstance()
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        { _, year, month, day ->
            val month = month + 1
            val date = "$day/$month/$year"
            val msg = "$date"
            dateText.text = msg
        }

        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            showTime(hourOfDay,minute)
        }

        bookButton.setOnClickListener {
            confirmBooking(description.text.toString())
        }

    }

    private fun confirmBooking(description:String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to confirm your booking?")
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id ->
                   makePayment()
                    saveData(description)

                })
            .setNegativeButton("cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })

        builder.create()
        builder.show()
    }

    private fun makePayment() {

        val checkout = Checkout()

        checkout.setKeyID("bJ8GUQICCVdlcW8KdN7LxAsZ")

        checkout.setImage(R.drawable.logo)

        val activity: Activity = this

        try {
            val options = JSONObject()
            options.put("name", "Abhirya Production")
            options.put("description", "Reference No. #123456")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
         //   options.put("order_id", "order_DBJOWzybf0sJbb") //from response of step 3.
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("amount", "50000") //pass amount in currency subunits
            options.put("prefill.email", "bhavate.arya@gmail.com")
            options.put("prefill.contact", "7020852743")
            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)
            checkout.open(activity, options)
        } catch (e: Exception) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e)
        }
    }

    private fun saveData(description: String) {
      /*  Toast.makeText(this,"Your Work Successfully Booked!",Toast.LENGTH_SHORT).show()
        val intent = Intent(this, CongratulationActivity::class.java)
        startActivity(intent)*/

        saveDataDao = SavedDataDao()

            saveDataDao.workData(destinationText.text.toString(),timeText.text.toString(),description,selectedWorkType)

    }

    private fun showTime(hour: Int, min: Int) {
        var hour = hour
        when {
            hour == 0 -> {
                hour += 12
                format = "AM"
            }
            hour == 12 -> {
                format = "PM"
            }
            hour > 12 -> {
                hour -= 12
                format = "PM"
            }
            else -> {
                format = "AM"
            }
        }
        timeText.text = StringBuilder().append(hour).append(" : ").append(min)
            .append(" ").append(format)
    }

    fun updateState(view: View) {

        val redCircle = findViewById<ImageView>(R.id.selectedRedCircle)
        redCircle.visibility = View.VISIBLE

        selectedWorkType = view.contentDescription.toString()
        Toast.makeText(applicationContext,selectedWorkType, Toast.LENGTH_LONG).show()

        val constraintLayout =
            findViewById<ConstraintLayout>(R.id.selectWorkLayout)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            R.id.selectedRedCircle,
            ConstraintSet.RIGHT,
            view.id,
            ConstraintSet.RIGHT,
            0
        )
        constraintSet.connect(
            R.id.selectedRedCircle,
            ConstraintSet.TOP,
            view.id,
            ConstraintSet.TOP,
            0
        )
        constraintSet.connect(
            R.id.selectedRedCircle,
            ConstraintSet.LEFT,
            view.id,
            ConstraintSet.LEFT,
            0
        )
        constraintSet.connect(
            R.id.selectedRedCircle,
            ConstraintSet.RIGHT,
            view.id,
            ConstraintSet.RIGHT,
            0
        )
        constraintSet.applyTo(constraintLayout)
    }

    override fun onPaymentError(p0: Int, p1: String?) {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("sUCCESSFULLL \n" +
                " $p0 and $p1").setPositiveButton("Yes",
                    DialogInterface.OnClickListener{ dialog, id ->

                    })
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(applicationContext,p0, Toast.LENGTH_LONG).show()
        val builder = AlertDialog.Builder(this)
        builder.setMessage("$p0").setPositiveButton("Yes",
            DialogInterface.OnClickListener{ dialog, id ->

            })
    }


}