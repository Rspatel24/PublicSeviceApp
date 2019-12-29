package com.example.contr.publicservice

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.contr.publicservice.Login.NavigationActivity
import com.example.contr.publicservice.Services.Mylocation
import com.example.contr.publicservice.Session.MyPref
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_set_ward_zone.*
import java.util.*

@Suppress("CAST_NEVER_SUCCEEDS")
class Set_ward_zone : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        ward1.setText(array[position])
        selectedward=array[position]
    }

    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    var storageReference2: StorageReference? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var databaseReference2: DatabaseReference? = null
    var databaseReference3: DatabaseReference? = null
    var pincode = ""
    var zone1 = ""
    var ward = ""
    lateinit var array: List<String>
    var eid: String? = null
    var fname: String? = null
    var lname: String? = null
    var password: String? = null
    var date_time: String? = null
    var cc1: String? = null
    var cc2: String? = null
    var key: String? = null
    var fullname: String? = null
    var imagepath: String? = null
    var videopath: String? = null
    var t: String? = null
    var l1=""

    var longitude: Double = 0.0
    var latitute: Double = 0.0

    var imguri: String? = null
    var vdouri: String? = null
    var hit: Int = 0
    var imguri2: Uri? = null
    var vdouri2: Uri? = null
        var accepted="no"
    var acceptingdate=""
    var acceptingtime=""
    var notify="false"
    var empnotify="no"
    var a: String? = null
    var b: String? = null
    var highauthority="no"
    var dtls: String? = null
    var date=""
    var time=""
    var selectedward=""
    lateinit var myPref: MyPref


    override fun onPause() {
        super.onPause()

    }

    override fun onClick(v: View?) {
        if (v == btnsubmit) {
            var key: String = databaseReference?.push()?.key.toString()
            var map = HashMap<String, String>()
            map.put("key", key)
            // map.put("demo","demo2")
            map.put("eid", eid.toString())
            map.put("fname", fname.toString())
            map.put("lname", lname.toString())
            map.put("fullname", fname.toString() + " " + lname.toString())
            map.put("complaincatogary", cc1!!)
            map.put("complaincode", cc2!!)
            map.put("complain_des", dtls!!)
            map.put("date&time", date_time.toString())
            map.put("imagepath", imagepath!!)
            map.put("videopath", videopath!!)
            map.put("accepted",accepted)
            map.put("acceptingdate",acceptingdate)
            map.put("acceptingtime",acceptingtime)
            map.put("notify",notify)
            map.put("empnotify",empnotify)
            map.put("date",date)
            map.put("time",time)
            //   Log.e("longisetwz",map.put("longtitude",b!!).toString())
            //  Log.e("latisetwz", map.put("latitude",a!!).toString())
            map.put("longtitude", b!!)
            map.put("latitude", a!!)
            map.put("realtime", t!!)
            map.put("highauthority",highauthority)
            map.put("hit", hit.toString())
            map.put("zone",zone1)
            map.put("ward",ward1.text.toString())
            map.put("solved","no")

            // Log.e("keysub",key)
            Log.e("eidsub", eid)
            Log.e("fnamesub", fname)
            Log.e("lnamesub", lname)
            Log.e("fullsub", fullname)
            Log.e("cc1sub", cc1)
            Log.e("cc2sub", cc2)
            Log.e("d&tsub", date_time)
            Log.e("imgsub", imagepath)
            Log.e("vdosub", videopath)
            Log.e("imgurisub", imguri2.toString())
            Log.e("vdourisub", vdouri2.toString())
            Log.e("data", map.toString())

            databaseReference?.child(key)?.setValue(map)

            var pDialog = ProgressDialog(this)
            pDialog.setTitle("Uploading Complain...")
            pDialog.setCancelable(true)
            pDialog.show()


            val childref = storageReference2?.child(videopath!!)
            val childref2 = storageReference?.child(imagepath!!)
            val uploadTask = childref?.putFile(vdouri2!!)
            val uploadTask2 = childref2?.putFile(imguri2!!)


            uploadTask2?.addOnSuccessListener {
                uploadTask?.addOnSuccessListener {
                }
                pDialog.dismiss()
                var builder = AlertDialog.Builder(this)
                builder.setTitle("Complain has been submitted !")
                    .setPositiveButton("ok") { dialog, id: Int ->
                        var intent = Intent(this, NavigationActivity::class.java)
                        startActivity(intent)

                    }
                    //.setNeutralButton("ok") { dialog, which ->
                    // }
                    .create().show()

            }

        } else if (v == btncancel) {
            var intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_ward_zone)




        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("Mycomplains")
        databaseReference2 = firebaseDatabase?.getReference("MyZone")
        databaseReference3 = firebaseDatabase?.getReference("MyWard")
        databaseReference2!!.orderByChild("MyZone")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    var child = p0.children
                    child.forEach {
                        l1 = it.value.toString()
                        Log.e("l111", l1.toString())
                    }
                }

            })



        myPref = MyPref(this)
        a = myPref.getlati().toString()
        b = myPref.getlongi().toString()
        pincode = myPref.getpincode().toString()
        Log.e("pincode ::", pincode)

        databaseReference2!!.orderByChild("MyZone")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    var child = p0.children
                    child.forEach {
                        Log.e("itvalue", it.key.toString())
                        if (it.key.equals(pincode)) {
                            zone1 = it.value.toString()
                            Log.e("zone ::", zone1)
                            zone.setText(zone1)
                        }

                    }
                }

            })

        databaseReference3!!.orderByChild("MyZone")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    var child = p0.children
                    child.forEach {
                        Log.e("itvalue", it.key.toString())
                        if (it.key.equals(pincode)) {
                            ward = it.value.toString()
                            Log.e("ward ::", ward)
                            array = ward.split("/")
                            var adapter = ArrayAdapter(this@Set_ward_zone, android.R.layout.simple_spinner_item, array)
                            wardspinner.adapter = adapter

                        }

                    }
                }

            })


        Log.e("afrommypref", a)
        Log.e("bformmypref", b)
        valoflati.setText(a)
        valoflongi.setText(b)

        Log.e("firebasedddddddtttt", firebaseDatabase.toString())
        storage = FirebaseStorage.getInstance()
        storageReference = storage?.getReferenceFromUrl("gs://mypublicservice-98437.appspot.com/complain_images/")
        storageReference2 = storage?.getReferenceFromUrl("gs://mypublicservice-98437.appspot.com/complain_videos/")


        var bundle = intent.extras
        key = bundle?.getString("key").toString()
        eid = bundle?.getString("eid").toString()
        fname = bundle?.getString("fname").toString()
        lname = bundle?.getString("lname").toString()
        fullname = bundle?.getString("fullname").toString()
        cc1 = bundle?.getString("complaincatogary").toString()
        cc2 = bundle?.getString("complaincode").toString()
        date_time = bundle?.getString("date&time").toString()
        imagepath = bundle?.getString("imagepath").toString()
        videopath = bundle?.getString("videopath").toString()
        dtls = bundle?.getString("comp_addrs").toString()
        t = bundle?.getString("realtime").toString()
        time=bundle?.getString("time").toString()
        date=bundle?.getString("date").toString()

        // longitude=bundle?.getString("longitutde").toString()
        // latitute=bundle?.getDouble("latitute").toString()
        imguri = bundle?.getString("imguri").toString()
        vdouri = bundle?.getString("vdouri").toString()
        imguri2 = intent.getParcelableExtra<Uri>("imguripar")
        vdouri2 = intent.getParcelableExtra<Uri>("vdouripar")

        //Log.e("keyfromsetw/d",key)
        Log.e("eidw/d", eid)
        Log.e("fnamew/d", fname)
        Log.e("lnamew/d", lname)
        Log.e("fullw/d", fullname)
        Log.e("cc1w/d", cc1)
        Log.e("cc2w/d", cc2)
        Log.e("d&tw/d", date_time)
        Log.e("imgw/d", imagepath)
        Log.e("vdow/d", videopath)
        Log.e("imguriw/d", imguri2.toString())
        Log.e("vdouriw/d", vdouri2.toString())
        Log.e("comp_addrs from set", dtls)

        btnsubmit.setOnClickListener(this)
        btncancel.setOnClickListener(this)
        wardspinner.onItemSelectedListener = this
    }
}
