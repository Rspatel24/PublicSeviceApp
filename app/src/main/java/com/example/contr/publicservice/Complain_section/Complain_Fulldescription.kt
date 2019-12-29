package com.example.contr.publicservice.Complain_section

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.contr.publicservice.Login.NavigationActivity
import com.example.contr.publicservice.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_complain__fulldescription.*
import java.io.File

var fullname:String?=null
var cc1:String?=null
var cc2:String?=null
var imgpath:String?=null
var vdopath:String?=null
var zone:String?=null
var ward:String?=null
var compdes:String?=null

var firebaseDatabase: FirebaseDatabase? = null
var databaseReference: DatabaseReference? = null
var storage: FirebaseStorage?=null
var storageReference: StorageReference?=null
var storageReference2: StorageReference?=null
var map:HashMap<String,String>?=null

class Complain_Fulldescription : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        if (v==fulldes_btnback)
        {
            var intent=Intent(this,NavigationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complain__fulldescription)

        fulldes_btnback.setOnClickListener(this)
        storage= FirebaseStorage.getInstance()
        storageReference = storage?.getReferenceFromUrl("gs://mypublicservice-98437.appspot.com/complain_images/")
        storageReference2 = storage?.getReferenceFromUrl("gs://mypublicservice-98437.appspot.com/complain_videos/")

        var bundle=intent.extras
        fullname=bundle?.getString("fullname").toString()
        cc1=bundle?.getString("comp_c1").toString()
        cc2=bundle?.getString("comp_c2").toString()
        imgpath=bundle?.getString("imgpath").toString()
        vdopath=bundle?.getString("videopath").toString()
        zone=bundle?.getString("zone").toString()
        ward=bundle?.getString("ward").toString()
        compdes=bundle?.getString("compdes").toString()

        val child = storageReference?.child(imgpath!!)
        Log.e("child",child.toString())
        try {

            val localFile = File.createTempFile(imgpath,null)
            Log.e("localfile",localFile.toString())
            //child?.downloadUrl
            child?.getFile(localFile)?.addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                fulldes_complain_image.setImageBitmap(bitmap)

            }?.addOnFailureListener { }

        } catch (e: Exception) {
            e.printStackTrace()
        }
       // complain_image.setImageURI(imgpath)
        fulldes_comp_code1.setText(cc1)
        fulldes_comp_code2.setText(cc2)
        fulldes_fullname.setText(fullname)
        fulldes_ward.setText(ward)
        fulldes_zone.setText(zone)
        complain_des.setText(compdes)
    }
}
