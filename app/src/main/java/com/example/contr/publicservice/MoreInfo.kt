package com.example.contr.publicservice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_more_info.*

class MoreInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_info)
        var bundle=intent.extras
        var empname=bundle.getString("ename1")
        var empdepartment=bundle.getString("empdepartment1")
        var mobno=bundle.getString("emobno1")
        Log.e("ename",empname)
        Log.e("edept",empdepartment)
        Log.e("mobno",mobno)
        txtname.setText(empname)
        txtdepartment.setText(empdepartment)
        txtmobno.setText(mobno)
        Log.e("In intent:","moreinfo")
    }
}
