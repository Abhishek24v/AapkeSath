package com.example.mainwork

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import com.example.mainwork.authentication.WelcomeActivity
import com.example.mainwork.daos.UserProfile
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    val END_SCALE = 0.7f

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var menuIcon: ImageView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        val currentUserPhoneNo = auth.currentUser?.phoneNumber.toString()
        val dbRef = FirebaseDatabase.getInstance().reference.child("User/$currentUserPhoneNo")
        dbRef.child("userName").get().addOnSuccessListener {
            userNameDisplay.text = it.value.toString()
        }.addOnFailureListener {
        }

        drawerLayout = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.navigation)
        menuIcon = findViewById(R.id.menuIcon)

        navigationDrawer()

        workButton.setOnClickListener {
            val intent = Intent(applicationContext, SelectWork::class.java)
            startActivity(intent)
        }

        workTravel.setOnClickListener {
            val intent = Intent(applicationContext, selectTravel::class.java)
            startActivity(intent)
        }
    }

    private fun navigationDrawer() {
        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.navigation_home)

        menuIcon.setOnClickListener() {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) drawerLayout.closeDrawer(
                GravityCompat.START
            )
            else drawerLayout.openDrawer(GravityCompat.START)
        }

        animateNavigationDrawer()
    }

    private fun animateNavigationDrawer() {

        drawerLayout.setScrimColor(resources.getColor(R.color.officialRed))
        drawerLayout.addDrawerListener(object : SimpleDrawerListener() {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                // Scale the View based on current slide offset
                val diffScaledOffset: Float = slideOffset * (1 - END_SCALE)
                val offsetScale = 1 - diffScaledOffset
                contentView.scaleX = offsetScale
                contentView.scaleY = offsetScale

                // Translate the View, accounting for the scaled width
                val xOffset: Float = drawerView.width * slideOffset
                val xOffsetDiff: Float = contentView.width * diffScaledOffset / 2
                val xTranslation = xOffset - xOffsetDiff
                contentView.translationX = xTranslation
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val intent = Intent(applicationContext, WelcomeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.navigation_home -> {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.navigation_work -> {
                val intent = Intent(applicationContext, SelectWork::class.java)
                startActivity(intent)
            }
            R.id.navigation_travel -> {
                val intent = Intent(applicationContext, selectTravel::class.java)
                startActivity(intent)
            }
            R.id.profile -> {
                val intent = Intent(applicationContext, UserProfile::class.java)
                startActivity(intent)
            }
            R.id.logout -> {
                openLogOutAlertBox()
            }
        }
            return true
    }


    private fun openLogOutAlertBox()  {

        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.exit)
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id ->
                    signOutFun()
                    val intent = Intent (this,WelcomeActivity::class.java)
                    startActivity(intent)
                })
            .setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })

        builder.create()
        builder.show()
    }

    private fun signOutFun() {
        FirebaseAuth.getInstance().signOut()
    }

}
