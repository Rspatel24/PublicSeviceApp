package com.example.contr.publicservice.Login

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.contr.publicservice.R
import com.google.android.gms.common.SignInButton
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class RegistrationActivity : AppCompatActivity(), View.OnClickListener {
    var lstnum = ArrayList<HashMap<String, String>>()
    var firebaseDatabase: FirebaseDatabase? = null
    lateinit var pDialog: ProgressDialog

    var databaseReference: DatabaseReference? = null
    var map: HashMap<String, String>? = null
    private var phoneVerificationId: String? = null
    private var verificationCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var fbauth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("Mypublicservice")
        edtfirstname.isEnabled = false
        edtlastname.isEnabled = false
        edtemailid.isEnabled = false
        edtpassword2.isEnabled = false
        edtrepassword.isEnabled = false
        edtaddress.isEnabled = false
        edtpincode.isEnabled = false
        btnsubmit.isEnabled = false
        fbauth = FirebaseAuth.getInstance()

        databaseReference!!.orderByChild("mobileno")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("Firebase Error", p0.toString())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach {
                        var map = it.value as HashMap<String, String>
                        lstnum.add(map)
                    }
                }
            })
        btnsubmit.setOnClickListener(this)
        btncancel.setOnClickListener(this)
    }

    fun send_otp(view: View) {
        var flag = 0
        pDialog = ProgressDialog(this@RegistrationActivity)
        pDialog.setMessage("please wait...")
        pDialog.setCancelable(true)
        pDialog.show()
        if (edtmobnum.text.trim().length == 0) {
            edtmobnum.setError("can not be blank")
            flag = 1
        } else if (edtmobnum.text.trim().length < 10) {
            edtmobnum.setError("10 digit required")
            flag = 1
        }
        if (flag != 1) {
            for (i in lstnum) {
                if ((i.get("mobileno").toString()).equals(edtmobnum.text.toString())) {
                    edtmobnum.setError("Mobile number already registered")
                    flag = 1
                }
            }
        }
        Log.e("flag value send-otp::", flag.toString())
        if (flag == 1) {
            pDialog.dismiss()
            return
        } else if (flag != 1) {
            Log.e("flag value in else::", flag.toString())
            if (view == btnsend) {
                btnsend.isEnabled=false
                send_otp2()
            } else if (view == btnresendotp)
                resend_otp()
        }
    }

    fun send_otp2() {
        Log.e("method", "sendcode")
        val phonenumber = "+91" + edtmobnum.getText().toString()
        setUpVerificationcallbacks()
        PhoneAuthProvider.getInstance()
            .verifyPhoneNumber(phonenumber, 60, TimeUnit.SECONDS, this, verificationCallbacks!!)
    }

    fun resend_otp() {
        val phonenumber = "+91" + edtmobnum.text.toString()
        setUpVerificationcallbacks()
        PhoneAuthProvider.getInstance()
            .verifyPhoneNumber(
                phonenumber,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks!!,
                resendToken
            )
    }


    fun setUpVerificationcallbacks() {
        verificationCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Log.e("method", "on verification complete")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e("verification failed..", e.message.toString())
            }

            override fun onCodeSent(
                verificationId: String?,
                ResendingToken: PhoneAuthProvider.ForceResendingToken?
            ) {
                super.onCodeSent(verificationId, ResendingToken)
                Log.e("method", "oncodesent")
                btnsend.isEnabled=false
                pDialog.dismiss()
                phoneVerificationId = verificationId
                resendToken = ResendingToken
            }
        }
    }

    fun verify(view: View) {
        var flag: Int = 0
        if (edtmobnum.text.trim().length == 0) {
            edtmobnum.setError("can not be blank")
            flag = 1
        } else if (edtmobnum.text.trim().length < 10) {
            edtmobnum.setError("10 digit required")
            flag = 1
        } else if (edtotp.text.toString().length == 0) {
            edtotp.setError("Please enter an OTP")
            flag = 1
        }
        if (flag == 1)
            return
        else {
            Log.e("method", "verify")
            val code = edtotp.text.toString()
            val credential = PhoneAuthProvider.getCredential(phoneVerificationId!!, code)
            signin(credential)
        }
    }

    fun signin(credential: PhoneAuthCredential) {
        Log.e("method", "signin")
        fbauth!!.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Successfully Verified..", Toast.LENGTH_LONG).show()
                edtotp.isEnabled = false
                edtmobnum.isEnabled = false
                btnresendotp.isEnabled = false
                btnsend.isEnabled = false
                btnverify.isEnabled = false
                edtfirstname.isEnabled = true
                edtlastname.isEnabled = true
                edtemailid.isEnabled = true
                edtpassword2.isEnabled = true
                edtrepassword.isEnabled = true
                edtaddress.isEnabled = true
                edtpincode.isEnabled = true
                btnsubmit.isEnabled = true
                val user = task.result!!.user
            } else {
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(this, "login failed..Invalid otp enter", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    override fun onClick(v: View?) {
        if (v == btnsubmit) {
            var flag: Int = 0
            if (edtfirstname.text.trim().length == 0) {
                edtfirstname.setError("can not be blank")
                flag = 1
            }
            if (edtlastname.text.trim().length == 0) {
                edtlastname.setError("can not be blank")
                flag = 1
            }
            if (edtemailid.text.trim().length == 0) {
                edtemailid.setError("can not be blank")
                flag = 1
            }
            if (!("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+").toRegex().matches(edtemailid.text.toString())) {
                edtemailid.setError("Invalid email-id")
                flag = 1
            } else {
                databaseReference!!.orderByChild("email-id").equalTo(edtemailid.text.toString())
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Log.e("Firebase Error", p0.toString())
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            p0.children.forEach {
                                var map = it.value as HashMap<String, String>
                                if (map.get("email-id").toString().equals(edtemailid.text.toString())) {
                                    flag = 1
                                    edtemailid.setError("E-mail id already exist.")
                                }
                            }
                        }
                    })
            }
            if (edtpassword2.text.trim().length == 0) {
                edtpassword2.setError("can not be blank")
                flag = 1
            }
            if (edtpincode.text.trim().length == 0) {
                edtpincode.setError("can not be blank")
                flag = 1
            }
            if (edtpincode.text.trim().length < 6) {
                edtpincode.setError("6 digit required")
                flag = 1
            }
            if (edtaddress.text.trim().length == 0) {
                edtaddress.setError("can not be blank")
                flag = 1
            }
            if (flag == 1) {
                return
            } else {
                var key: String = databaseReference?.push()?.key.toString()
                var map = HashMap<String, String>()
                map.put("key", key)
                map.put("firstname", edtfirstname.text.toString())
                map.put("lastname", edtlastname.text.toString())
                map.put("fullname", edtfirstname.text.toString() + " " + edtlastname.text.toString())
                map.put("email-id", edtemailid.text.toString())
                if (edtpassword2.text.toString() == edtrepassword.text.toString()) {
                    map.put("password", edtpassword2.text.toString())
                    map.put("mobileno", edtmobnum.text.toString())
                    map.put("address", edtaddress.text.toString())
                    map.put("pincode", edtpincode.text.toString())
                    databaseReference?.child(key)?.setValue(map)
                    Toast.makeText(this, "Registration Successfully", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    edtrepassword.setError("not match")
                }
            }
        }
        if (v == btncancel) {
            finish()
        }
    }

    companion object {
        private val TAG = "PhoneAuth"
    }
}