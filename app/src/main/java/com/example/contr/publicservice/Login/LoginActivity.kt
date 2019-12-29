package com.example.contr.publicservice.Login

import android.Manifest
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.contr.publicservice.Session.MyPref
import com.example.contr.publicservice.R
import com.example.contr.publicservice.Services.checkconnection
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var uname: Boolean = false
    var pass: Boolean = false

    private var STORAGE_PERMISSION_CODE = 1
    //var map:HashMap<String,String>?=null
    //var key:String=""


    override fun onClick(v: View?) {
        if (v == btnnewuser) {
            var intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)

        } else if (v == btnlogin) {
            var flag: Int = 0
            if (edtuser.text.trim().length == 0) {
                edtuser.setError("can not be blank")
                flag = 1
            }
            if(!("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+").toRegex().matches(edtuser.text.toString()))
            {
                edtuser.setError("Invalid Email-id")
                flag = 1
            }

            if (edtpassword.text.trim().length == 0) {
                edtpassword.setError("can not be blank")
                flag = 1
            }
            if (flag == 1) {
                return
            }
            var pDialog= ProgressDialog(this)
            pDialog.setMessage("please wait...")
            pDialog.setCancelable(true)
            pDialog.show()
            databaseReference!!.orderByChild("email-id").equalTo(edtuser.text.toString())
            databaseReference!!.orderByChild("password").equalTo(edtpassword.text.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.e("Firebase Error", p0.toString())
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        var flag: Int = 0
                        var userid: String? =null
                        var firstname: String?=null
                        var lastname:String?=null
                        var password:String?=null
                        p0.children.forEach {
                            var map = it.value as HashMap<String, String>
                            if (map.get("email-id").toString().equals(edtuser.text.toString()) && map.get("password").toString().equals(
                                    edtpassword.text.toString()
                                )
                            )
                            {
                                uname=true
                                pass=true
                                var bundle=Bundle()
                                userid=map.get("email-id").toString()
                                firstname=map.get("firstname").toString()
                                lastname=map.get("lastname").toString()
                                password=map.get("password").toString()
                                Log.e("userid",userid)
                                Log.e("firstname",firstname)
                                Log.e("lastname",lastname)

                                bundle.putString("firstname",firstname)
                                bundle.putString("lastname",lastname)
                                intent.putExtras(bundle)

                            }

                        }
                        if (uname && pass) {

                            myPref.setUserName(userid!!,firstname!!,lastname!!,password!!)
                            pDialog.dismiss()
                            var intent = Intent(
                                this@LoginActivity,
                                NavigationActivity::class.java)
                            startActivity(intent)

                        }
                        else
                        {
                            pDialog.dismiss()
                            var builder = AlertDialog.Builder(this@LoginActivity)
                            builder.setTitle("Username or Password invalid")
                                .setNeutralButton("ok",null)
                                .create().show()
                            // Toast.makeText(this@LoginActivity, "Username or Password invalid", Toast.LENGTH_SHORT).show()
                            //flag = 1

                        }

                    }
                })

        }
    }

    /*override fun onResume() {
        super.onResume()
        uname=false
        pass=false
    }*/

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {

            AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This permission is needed !!")
                .setPositiveButton("ok", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        ActivityCompat.requestPermissions(this@LoginActivity,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),STORAGE_PERMISSION_CODE)
                    }
                })
                .setNegativeButton("cancel", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        dialog.dismiss()
                    }
                })
                .create().show()

        }
        else
        {
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION),STORAGE_PERMISSION_CODE)
        }



    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    lateinit var myPref: MyPref
    lateinit var checkcon : checkconnection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
            Toast.makeText(this, "You have already granted this permission!",
                Toast.LENGTH_SHORT).show()
        }
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED ) {
            Toast.makeText(this, "You have already granted this permission!",
                Toast.LENGTH_SHORT).show()
        }
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Toast.makeText(this, "You have already granted this permission!",
                Toast.LENGTH_SHORT).show()
        }

        else
        {
            requestStoragePermission()
        }

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("Mypublicservice")

        checkcon=checkconnection()
        var isConnected=checkcon.check(this)
        Log.e("loginactivity",isConnected.toString())
        if (isConnected==false)
        {
            var builder = AlertDialog.Builder(this@LoginActivity)
            builder.setTitle("Check Your Internet Connection ")
                .setPositiveButton("Try again"){dialog, id:Int->
                    startActivity(intent)

                }
                .setNeutralButton("ok",null)
                .create().show()

        }
        else
        {
            myPref= MyPref(this)
            if(myPref.getUserName()!=null)
            {
                finish()
                startActivity(Intent(this,NavigationActivity::class.java))
            }
            btnnewuser.setOnClickListener(this)
            btnlogin.setOnClickListener(this)
        }

        // btngoogle.setOnClickListener(this)

    }
}




