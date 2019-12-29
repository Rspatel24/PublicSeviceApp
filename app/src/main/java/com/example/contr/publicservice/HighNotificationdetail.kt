package com.example.contr.publicservice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_high_notificationdetail.*
import kotlinx.android.synthetic.main.activity_more_info.*

class HighNotificationdetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_notificationdetail)

        var bundle=intent.extras
        var empname=bundle.getString("highempname")
        var empdepartment=bundle.getString("highempdepartment")
        var empid=bundle.getString("highempid")
        Log.e("ename",empname)
        Log.e("edept",empdepartment)


        txthighname.setText(empname)
        txthighdepartment.setText(empdepartment)
        txtempid.setText(empid)
        Log.e("In intent:","moreinfo")
    }
}
