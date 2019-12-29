package com.example.contr.publicservice

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.util.Log
import android.widget.Toast
import com.example.contr.publicservice.Fragments.GoogleMapFragment
import com.example.contr.publicservice.Services.Mylocation
import kotlinx.android.synthetic.main.activity_get__direction.*

class Get_Direction : AppCompatActivity() {


    var eid:String? =null
    var fname:String?=null
    var lname:String?=null
    var password:String?=null
    var date_time: String?=null
    var cc1:String?=null
    var cc2:String?=null
    var t:String?=null
    var key:String?=null
    var fullname:String?=null
    var imagepath:String?=null
    var videopath:String?=null
    var date=""
    var time=""
    var dtls:String?=null
    var imguri:String?=null
    var vdouri:String?=null

    var imguri2: Uri?=null
    var vdouri2:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get__direction)

        var gmf=GoogleMapFragment()
        var fragmentmanger: FragmentManager =supportFragmentManager
        fragmentmanger.beginTransaction().replace(R.id.get_direction,gmf).commit()

        var bundle=intent.extras
        key=bundle?.getString("key").toString()
        eid=bundle?.getString("eid").toString()
        fname=bundle?.getString("fname").toString()
        lname=bundle?.getString("lname").toString()
        fullname=bundle?.getString("fullname").toString()
        cc1=bundle?.getString("complaincatogary").toString()
        cc2=bundle?.getString("complaincode").toString()
        date_time=bundle?.getString("date&time").toString()
        imagepath=bundle?.getString("imagepath").toString()
        videopath=bundle?.getString("videopath").toString()
        t=bundle?.getString("realtime").toString()
        date=bundle?.getString("date").toString()
        time=bundle?.getString("time").toString()
        imguri2= intent.getParcelableExtra<Uri>("imguripar")
        vdouri2=intent.getParcelableExtra<Uri>("vdouripar")


        dtls=bundle?.getString("comp_addrs").toString()

        Log.e("keyget",key)
        Log.e("eidget",eid)
        Log.e("fnameget",fname)
        Log.e("lnameget",lname)
        Log.e("fullget",fullname)
        Log.e("cc1get",cc1)
        Log.e("cc2get",cc2)
        Log.e("d&tget",date_time)
        Log.e("imgget",imagepath)
        Log.e("vdoget",videopath)
        Log.e("imguriget",imguri2.toString())
        Log.e("vdouriget",vdouri2.toString())
        Log.e("comp_addrs from get_dir",dtls)


        getdirection_fab_refresh.setOnClickListener {
            Toast.makeText(this,"refresh",Toast.LENGTH_SHORT).show()


        }
        getdirection_fab2_next.setOnClickListener {

            var intent=Intent(this,Set_ward_zone::class.java)
            var bundle=Bundle()
            bundle.putString("key",key)
            bundle.putString("eid",eid.toString())
            bundle.putString("fname",fname.toString())
            bundle.putString("lname", lname.toString())
            bundle.putString("fullname",fname.toString()+" "+lname.toString())
            bundle.putString("complaincatogary", cc1)
            bundle.putString("complaincode", cc2)
            bundle.putString("date&time",date_time.toString())
            bundle.putString("imagepath",imagepath)
            bundle.putString("videopath",videopath)
            bundle.putString("comp_addrs",dtls)
            bundle.putString("realtime",t)
            bundle.putString("date",date)
            bundle.putString("time",time)

           // bundle.putString("googlelongi",longitude.toString())
           // bundle.putString("googlelati",latitute.toString())

            bundle.putParcelable("imguripar",imguri2)
            bundle.putParcelable("vdouripar",vdouri2)


          //  intent.putExtra("imguri",imguri2)
           // intent.putExtra("vdouri",vdouri2)
            intent.putExtras(bundle)
            startActivity(intent)





        }
    }

}
