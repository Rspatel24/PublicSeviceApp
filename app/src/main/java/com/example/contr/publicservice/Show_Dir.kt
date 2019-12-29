package com.example.contr.publicservice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.util.Log

class Show_Dir : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show__dir)

        var bundle=intent.extras
        val lati=bundle?.getDouble("lati")
        val longi=bundle?.getDouble("longi")
        val compcode=bundle?.getString("complaincode")

        Log.e("lati from show_dir",lati.toString())
        Log.e("longi from show_dir",longi.toString())

        var bundle2=Bundle()
        bundle2.putDouble("lati", lati!!)
        bundle2.putDouble("longi",longi!!)
        bundle2.putString("complaincode",compcode)

        var smap=Show_map()
        smap.arguments=bundle2

        var fragmentmanger: FragmentManager =supportFragmentManager
        fragmentmanger.beginTransaction().replace(R.id.show_direction,smap).commit()



    }
}
