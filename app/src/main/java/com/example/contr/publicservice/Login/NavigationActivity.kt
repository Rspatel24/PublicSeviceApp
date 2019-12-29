package com.example.contr.publicservice.Login

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.contr.publicservice.Complain_section.ComplainActivity
import com.example.contr.publicservice.Complain_section.Complain_User_Details
import com.example.contr.publicservice.Complain_section.Feedfragment
import com.example.contr.publicservice.Complain_section.Solved_Complain_details
import com.example.contr.publicservice.Fragments.GoogleMapFragment
import com.example.contr.publicservice.NotificationBroadcast
import com.example.contr.publicservice.Session.MyPref
import com.example.contr.publicservice.R
import com.example.contr.publicservice.Services.Mylocation
import com.example.contr.publicservice.Services.checkconnection
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.app_bar_navigation.*

class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var myPref: MyPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        setSupportActionBar(toolbar)


        lateinit var checkcon: checkconnection

        myPref = MyPref(this)

        checkcon = checkconnection()
        var isConnected = checkcon.check(this)
        Log.e("loginactivity", isConnected.toString())
        if (isConnected == false) {
            var builder = AlertDialog.Builder(this@NavigationActivity)
            builder.setTitle("Check Your Internet Connection ")
                .setPositiveButton("ok") { dialog, id: Int ->
                    finish()

                }

                .create().show()

        }

        var ff = Feedfragment()
        var gmf = GoogleMapFragment()
        var myloc = Mylocation()
        myloc.Mylistner()
        var fragmentmanger: FragmentManager = supportFragmentManager
        fragmentmanger.beginTransaction().replace(R.id.nav_main_view, ff).commit()

        fab.setOnClickListener { view ->
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()*/

            var intent = Intent(this, ComplainActivity::class.java)
            startActivity(intent)
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        var eid = myPref.getUserName()

        var fname = myPref.getfirstname()

        var lname = myPref.getlastname()

        var full = (fname + " " + lname)
        var tvEmailId = nav_view.getHeaderView(0).findViewById<TextView>(R.id.fullemailid)
        tvEmailId?.setText(eid)
        var tvfullname = nav_view.getHeaderView(0).findViewById<TextView>(R.id.fullname)
        tvfullname?.setText(full)
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_complains -> {

                var pDialog = ProgressDialog(this)
                pDialog.setMessage("please wait...")
                pDialog.setCancelable(true)
                pDialog.show()
                if (myPref.getUserName() != null) {
                    pDialog.dismiss()
                    startActivity(Intent(this, Complain_User_Details::class.java))
                }

            }
            R.id.nav_setting -> {

            }
            R.id.nav_feedback -> {

            }
            R.id.nav_solved_complain -> {
                var pDialog = ProgressDialog(this)
                pDialog.setMessage("please wait...")
                pDialog.setCancelable(true)
                pDialog.show()
                if (myPref.getUserName() != null) {
                    pDialog.dismiss()
                    startActivity(Intent(this, Solved_Complain_details::class.java))
                }

            }
            R.id.nav_logout -> {


                myPref = MyPref(this)

                var intent = Intent(this, LoginActivity::class.java)
                var builder = AlertDialog.Builder(this)
                    .setTitle("logout")
                    .setMessage("Are you sure to logout?")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                        if (myPref.getfirstname() != null) {

                            myPref.Logout()
                            startActivity(Intent(this, LoginActivity::class.java))
                            //stopService(Intent(this,NotificationSevice::class.java))
                            var intentcall: Intent = Intent(applicationContext, NotificationBroadcast::class.java)
                            var pendingintent: PendingIntent =
                                PendingIntent.getBroadcast(applicationContext, 2244, intentcall, 0)
                            var alarmManager: AlarmManager? = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                            alarmManager!!.cancel(pendingintent)
                            finish()
                        }
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> })
                    .create().show()


            }
            R.id.fab -> {


            }

        }


        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var bm: Bitmap = data?.extras?.get("data") as Bitmap

    }
}
