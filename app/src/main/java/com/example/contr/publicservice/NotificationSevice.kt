package com.example.contr.publicservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.TextView
import com.example.contr.publicservice.Session.MyPref
import com.google.firebase.database.*




class NotificationSevice: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var databaseReference2: DatabaseReference? = null
    var databaseReference3: DatabaseReference? = null
    var map: HashMap<String, String>? = null
    var map1: HashMap<String, String>? = null
    var map2: HashMap<String, String>? = null
    var accepted: String? = null
    var username: String? = null
    var eid: String? = null
    var isNotify=""
    var key=""
    var fullname=""
    var complaint=""
    var department=""
    var empdepartment=""
    var ename=""
    var emobno=""
    var highauthority=""
    var highname=""
    var highempid=""
    var highempdepartment=""

    lateinit var myPref: MyPref


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        myPref = MyPref(this)
        username = myPref.getUserName()
        Log.e("recdbref", databaseReference.toString())
        databaseReference!!.orderByChild("Mycomplains")

            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(p0: DataSnapshot) {

                    p0.children.forEach {
                        map = it.value as HashMap<String, String>
                        accepted = map!!.get("accepted").toString()
                        Log.e("recaccepted",accepted)
                        if (accepted == "yes") {
                            eid = map!!.get("eid").toString()
                            isNotify= map!!.get("notify").toString()
                            key=map!!.get("key").toString()
                            fullname=map!!.get("fullname").toString()
                            complaint=map!!.get("complaincode").toString()
                            department=map!!.get("complaincatogary").toString()
                            highauthority=map!!.get("highauthority").toString()
                            Log.e("eidd", eid)
                            Log.e("usernamee", username)
                            if (eid == username && isNotify=="false" ) {
                                if(highauthority=="no" ) {
                                    showNotification()
                                }
                                if(highauthority=="yes") {
                                    showhighnotification()
                                }
                            }



                        }


                    }


                }
            })
        databaseReference2!!.orderByChild("MyEmployee")
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {

                    p0.children.forEach {
                        map1 = it.value as HashMap<String, String>
                        empdepartment= map1!!.get("department").toString()
                        if(department==empdepartment){
                            ename=map1!!.get("name").toString()
                            emobno=map1!!.get("mobno").toString()
                            empdepartment=map1!!.get("department").toString()
                            Log.e("ename---",ename)
                            Log.e("edept---",empdepartment)
                            Log.e("mobno---",emobno)




                        }



                    }
                }


            })
        databaseReference3!!.orderByChild("MyAuthority")
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {

                    p0.children.forEach {
                        map2 = it.value as HashMap<String, String>
                        empdepartment= map2!!.get("department").toString()
                        if(department==empdepartment){
                            highname=map2!!.get("name").toString()
                            highempid=map2!!.get("empid").toString()
                            highempdepartment=map2!!.get("department").toString()




                        }



                    }
                }


            })


       return super.onStartCommand(intent, flags, startId)
    }
    override fun onCreate() {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("Mycomplains")
        databaseReference2=firebaseDatabase?.getReference("MyEmployee")
        databaseReference3=firebaseDatabase?.getReference("MyAuthority")
        Log.e("dbref", databaseReference.toString())
        super.onCreate()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification() {
        var notificid = 101
        Log.e("in notific:", "notification")
        var bundle= Bundle()
        bundle.putString("ename1",ename)
        bundle.putString("emobno1",emobno)
        bundle.putString("empdepartment1",empdepartment)
        var notificbuilder = NotificationCompat.Builder(this)
        var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        /*var id = "my_channel_01"
        var CHANNEL_ID = "my_channel_01"
        var name = "201"
        var description="301"
        var importance = NotificationManager.IMPORTANCE_LOW
        var mChannel = NotificationChannel(id, name, importance)
        mChannel.description = description
        mChannel.enableLights(true)
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(true)
        mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        manager.createNotificationChannel(mChannel)*/
        notificbuilder.setContentTitle("Dear $fullname")
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_light_normal)
            .setContentText("Your complaint for ${complaint} accepted ")
            .setTicker("New Notification")
            .setAutoCancel(true)
           // .setChannelId(CHANNEL_ID)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500))
        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        var moreinfointent = Intent(this, MoreInfo::class.java)
        moreinfointent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
         moreinfointent.putExtras(bundle)
        // var tstackbuilder=TaskStackBuilder.create(context)
        //tstackbuilder.addParentStack(Moreinfo.class)
        //tstackbuilder.addNextIntent(moreinfointent)
        var pendingintent = PendingIntent.getActivity(this, 0, moreinfointent, PendingIntent.FLAG_UPDATE_CURRENT)
        notificbuilder.setContentIntent(pendingintent)

        manager.notify(notificid, notificbuilder.build())

        databaseReference!!.child(key).child("notify").setValue("true")
        Log.e("after notific:", "notification")
    }
    fun showhighnotification() {
        var notificid = 102
        Log.e("in high::::::::notific:", "notification")
        var bundle= Bundle()
        bundle.putString("highempname",highname)
        bundle.putString("highempid",highempid)
        bundle.putString("highempdepartment",highempdepartment)
        var notificbuilder = NotificationCompat.Builder(this)
        var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        /*var id = "my_channel_01"
        var CHANNEL_ID = "my_channel_01"
        var name = "201"
        var description="301"
        var importance = NotificationManager.IMPORTANCE_LOW
        var mChannel = NotificationChannel(id, name, importance)
        mChannel.description = description
        mChannel.enableLights(true)
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(true)
        mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        manager.createNotificationChannel(mChannel)*/
        notificbuilder.setContentTitle("Dear $fullname")
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_light_normal)
            .setContentText("Your complaint for ${complaint} is transfered to the HighAuthority ")
            .setTicker("New Notification")
            .setAutoCancel(true)
            // .setChannelId(CHANNEL_ID)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500))
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        var moreinfointent = Intent(this, HighNotificationdetail::class.java)
        moreinfointent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        moreinfointent.putExtras(bundle)
        // var tstackbuilder=TaskStackBuilder.create(context)
        //tstackbuilder.addParentStack(Moreinfo.class)
        //tstackbuilder.addNextIntent(moreinfointent)
        var pendingintent = PendingIntent.getActivity(this, 0, moreinfointent, PendingIntent.FLAG_UPDATE_CURRENT)
        notificbuilder.setContentIntent(pendingintent)

        manager.notify(notificid, notificbuilder.build())

        databaseReference!!.child(key).child("notify").setValue("true")
        Log.e("after notific:", "notification")
    }
}