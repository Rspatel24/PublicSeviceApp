package com.example.contr.publicservice

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import com.example.contr.publicservice.Login.LoginActivity
import com.example.contr.publicservice.Login.NavigationActivity
import com.example.contr.publicservice.Session.MyPref
import java.util.*

class Main2Activity : AppCompatActivity(){
    lateinit var myPref: MyPref
    var isConnected:Boolean = false


        var permission_all:Int?=1
        var permission_array= arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.READ_EXTERNAL_STORAGE)


    fun permission(context: Context){
        
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val connectivityManager =
            getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        isConnected = activeNetwork?.isConnectedOrConnecting == true
            Log.e("isConnected", isConnected.toString())

            myPref = MyPref(this)

            var pDialog = ProgressDialog(this@Main2Activity)
            pDialog.setMessage("please wait...")
            pDialog.setCancelable(true)
            pDialog.show()


            if (isConnected)
            {

                if (myPref.getUserName() != null) {
                    pDialog.dismiss()
                    var intent = Intent(this, NavigationActivity::class.java)
                    startActivity(intent)
                }
                else {
                    val mythread = object : Thread() {
                        override fun run() = try {
                            Thread.sleep(2000)

                            var intent = Intent(this@Main2Activity, LoginActivity::class.java)
                            startActivity(intent)
                           // finish()

                        } catch (e: Exception) {

                        }
                    }
                    mythread.start()
                }
            }
            else {
                pDialog.dismiss()
                var builder = AlertDialog.Builder(this@Main2Activity)
                builder.setTitle("No Internet Connection ")
                    .setPositiveButton("ok"){dialog, id:Int->
                        finish()

                    }
                    .create().show()


            }
        }
    }








