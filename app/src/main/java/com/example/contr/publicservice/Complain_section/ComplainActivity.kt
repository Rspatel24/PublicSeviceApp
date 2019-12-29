package com.example.contr.publicservice.Complain_section

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.*
import com.example.contr.publicservice.Fragments.GoogleMapFragment
import com.example.contr.publicservice.Get_Direction
import com.example.contr.publicservice.Login.NavigationActivity
import com.example.contr.publicservice.R
import com.example.contr.publicservice.Session.MyPref
import kotlinx.android.synthetic.main.activity_complain.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ComplainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var myPref: MyPref

    var s: String? = null
    var eid: String? = null
    var fname: String? = null
    var lname: String? = null
    var password: String? = null
    var dtls: String? = null
    var date=""

    var time: Date? = null

    var time2: Date? = null

    var comp_lati: Double? = null
    var comp_longi: Double? = null

    var bm: Bitmap? = null
    var bm2: Bitmap? = null
    var fp: Uri? = null
    var fp2: Uri? = null
    var newfp: Uri? = null
    var path: String = ""
    var videouri: Uri? = null
    var videouri2: Uri? = null
    var dat=""

    var map: HashMap<String, String>? = null
    var comp = arrayOfNulls<String>(10)

    var flag = 0
    var flag2 = 0
    var compdes=""

    var gmf2 = GoogleMapFragment()


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        // Log.e("onsavedstate","savedstate")

        if (fp != null) {
            fp2 = fp
            outState?.putParcelable("imageuri", fp2)
        }
        if (bm != null) {
            bm2 = bm
            outState?.putParcelable("image", bm2)
            Log.e("onsaved bm2", bm2.toString())
        }
        if (videouri != null) {
            videouri2 = videouri
            outState?.putParcelable("video", videouri2)
            Log.e("onsaved video", videouri2.toString())
        }
        if (flag2 == 1) {
            bm = null
            videouri = null
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        //Log.e("restore","restore2")

        fp = savedInstanceState?.getParcelable("imageuri")
        bm = savedInstanceState?.getParcelable("image")
        videouri = savedInstanceState?.getParcelable("video")
        if (fp != null) {

        }
        if (bm != null) {
            complain_image.setImageBitmap(bm)
            Log.e("restore bm", bm.toString())
        }
        if (videouri != null) {
            complain_video.setVideoURI(videouri)
            Log.e("restore video", videouri.toString())


        }
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        if (position == 0) {
            comp = arrayOf("Cleaning not proper", "Container not cleaned proper")
        } else if (position == 1) {
            comp =
                arrayOf("Overflowing drainage within house", "Leakage in drainage line", "Missing/Broken Manhole cover")
        } else if (position == 2) {
            comp = arrayOf("Insufficient lighting", "Street light not working")
        } else if (position == 3) {
            comp = arrayOf(
                "Insufficient supply duration",
                "Pipeline leakage",
                "Supply not received",
                "Repairing of handpump"
            )
        }
        //s= parent?.getItemAtPosition(position) as String?
        //Log.e("sss",s.toString())
        var adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, comp)
        complain_code.setAdapter(adapter2)
        //complain_code.onItemSelectedListener=this

    }

    // var complaincatogary: ArrayList<String> = arrayListOf("garbage problem","Dranage problem","street light","littering problem","water problem")
    //var complaincode:ArrayList<String> = arrayListOf("leakage problem","cleaning not proper","Street light is not working","Repairing of handpump")

    override fun onClick(v: View?) {
        //  myPref= MyPref(this)
        var cc1 = complain_catogary.selectedItem.toString()
        var cc2 = complain_code.selectedItem.toString()
        // Log.e("cc1",cc1)
        // Log.e("cc2",cc2)

        if (v == btntakephoto) {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 111)
        } else if (v == btntakevideo) {
            var intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(intent, 112)
        } else if (v == btncancel) {
            var intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
        } else if (v == btnNext) {
            flag = 0
            Log.e("value of flag", flag.toString())
            if (fp == null) {
                flag = 1
                Log.e("value of flag in fp", flag.toString())
                var builder = AlertDialog.Builder(this@ComplainActivity)
                builder.setTitle("Image is Required ")
                    /* .setPositiveButton("ok"){dialog, id:Int->
                         finish()
                     }*/
                    .setNeutralButton("ok") { dialog, which ->
                    }
                    .create().show()
            } else if (videouri == null) {
                flag = 1
                Log.e("value of flag in video", flag.toString())
                var builder = AlertDialog.Builder(this@ComplainActivity)
                builder.setTitle("Video is Required ")
                    /* .setPositiveButton("ok"){dialog, id:Int->
                         finish()
                     }*/
                    .setNeutralButton("ok") { dialog, which ->
                    }
                    .create().show()

            }
            if (flag == 1) {
                return
            }

            var imagepath = fp!!.lastPathSegment
            var videopath = videouri!!.lastPathSegment
            time = Calendar.getInstance().time


            var d = Date()
            var c = SimpleDateFormat("hh:mm a")
            var c2 = SimpleDateFormat("dd/MM/yyyy")
            time2 = Calendar.getInstance().time

            var t2 = c2.format(time2)
            var t: String = c.format(d)
            Log.e("realdate", t2.toString())
            Log.e("realtime", t)


            dtls = edtaddress.text.toString()
            var intent = Intent(this@ComplainActivity, Get_Direction::class.java)
            var bundle = Bundle()
            // bundle.putString("key",key)
            bundle.putString("eid", eid.toString())
            bundle.putString("fname", fname.toString())
            bundle.putString("lname", lname.toString())
            bundle.putString("fullname", fname.toString() + " " + lname.toString())
            bundle.putString("complaincatogary", cc1)
            bundle.putString("complaincode", cc2)
            bundle.putString("date&time", time.toString())
            bundle.putString("imagepath", imagepath)
            bundle.putString("videopath", videopath)
            bundle.putString("comp_addrs", dtls)
            bundle.putString("realtime", "$t2 $t")
            bundle.putString("date",t2)
            bundle.putString("time",t)

            // bundle.putString("imguri",fp.toString())
            // bundle.putString("vdouri",videopath.toString())

            bundle.putParcelable("imguripar", fp)
            bundle.putParcelable("vdouripar", videouri)

            // intent.putExtra("imguri",fp.toString())
            // intent.putExtra("vdouri",videouri.toString())
            intent.putExtras(bundle)
            startActivity(intent)


        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complain)


        /*   firebaseDatabase= FirebaseDatabase.getInstance()
           databaseReference=firebaseDatabase?.getReference("Mycomplains")

           storage= FirebaseStorage.getInstance()
           storageReference = storage?.getReferenceFromUrl("gs://mypublicservice-98437.appspot.com/complain_images/")
           storageReference2 = storage?.getReferenceFromUrl("gs://mypublicservice-98437.appspot.com/complain_videos/")*/


        myPref = MyPref(this)
        eid = myPref.getUserName()
        fname = myPref.getfirstname()
        lname = myPref.getlastname()
        password = myPref.getpassword()

        Log.e("compusername", eid.toString())
        Log.e("compuserpassword", password.toString())
        complain_catogary.onItemSelectedListener = this

        /* var adapter=ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,comp)
         complain_catogary.setAdapter(adapter)
         complain_catogary.onItemSelectedListener=this*/
        btntakephoto.setOnClickListener(this)
        btncancel.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        btntakevideo.setOnClickListener(this)

    }

    fun getimguri(context: Context, bm: Bitmap): Uri? {
        var bytes: ByteArrayOutputStream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        var path: String = MediaStore.Images.Media.insertImage(context.getContentResolver(), bm, "Title", null)

        return Uri.parse(path)

    }

    fun getrealpath(uri: Uri): String {
        var st: String = ""
        if (contentResolver != null) {
            var cursor: Cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                var idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }

        return path
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && requestCode == 111) {

            bm = data?.extras?.get("data") as Bitmap
            complain_image.setImageBitmap(bm)
            //fp=data.data
            newfp = getimguri(applicationContext, bm!!)
            var finalfile: File = File(getrealpath(this!!.newfp!!))
            fp = newfp

            Log.e("imageuri", fp.toString())

        } else if (data != null && requestCode == 112) {
            videouri = data.data
            Log.e("videouri", videouri.toString())
            complain_video.setVideoURI(videouri)
        }

    }


}

