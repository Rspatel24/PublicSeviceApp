package com.example.contr.publicservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.contr.publicservice.Session.MyPref

class NotificationBroadcast :BroadcastReceiver() {

    lateinit var myPref: MyPref
    var username:String?=null
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("receiver::", "Noti")

        myPref= MyPref(context!!)
         username=myPref.getUserName().toString()
        Log.e("uname==",username)
        if(username!=null) {
            val intent1 = Intent(context, NotificationSevice::class.java)

                context.startService(intent1)


        }



    }
}