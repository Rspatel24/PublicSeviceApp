package com.example.contr.publicservice.Complain_section

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.contr.publicservice.NotificationBroadcast
import com.example.contr.publicservice.R
import com.example.contr.publicservice.Services.Mylocation
import com.example.contr.publicservice.Session.MyPref
import com.example.contr.publicservice.Show_Dir
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_feedfragment.*
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class Feedfragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        var ft = getFragmentManager()!!.beginTransaction();

        ft.detach(this@Feedfragment).attach(this@Feedfragment).commit();


    }

    inner class abc(
        internal var arrcomp: ArrayList<String>,
        internal var arrcomp1: ArrayList<String>,
        internal var arrcomp2: ArrayList<String>,
        internal var arrimg: ArrayList<String>,
        internal var arrvdo: ArrayList<String>,
        internal var loclati: ArrayList<String>,
        internal var loclongi: ArrayList<String>,

        internal var realtimearray: ArrayList<String>,

        internal var acceptstatusarray: ArrayList<String>,
        internal var wardarray: ArrayList<String>,
        internal var zonearray: ArrayList<String>,
        internal var compdesarray: ArrayList<String>,
        internal var acceptingdatearray: ArrayList<String>,
        internal var acceptingtimearray: ArrayList<String>
    ) : BaseAdapter() {
        var layoutInflater: LayoutInflater

        init {
            layoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        }

        inner class ViewHolder {
            var imgktt: ImageView? = null
            var tvTitlekt: TextView? = null
            var tvComp_1: TextView? = null
            var tvComp_2: TextView? = null
            var tvtime: TextView? = null
            var tvaccept: TextView? = null
            var lyout: LinearLayout? = null
            var tvhitbtn: ToggleButton? = null
            var tvdetilsbtn: Button? = null
            var tvgetdir: ImageButton? = null
            var tvaccepttime: TextView? = null
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            //arrcomp.get(fullname as Int).toString()
            var viewHolder = ViewHolder()
            var view = layoutInflater.inflate(R.layout.feed, null)
            viewHolder.imgktt = view.findViewById(com.example.contr.publicservice.R.id.imgg1)
            viewHolder.tvTitlekt = view.findViewById(R.id.feedusername1)
            viewHolder.tvComp_1 = view.findViewById(R.id.complain_cat_1)
            viewHolder.tvComp_2 = view.findViewById(R.id.complain_cat_2)
            viewHolder.tvaccept = view.findViewById(R.id.acceptstatus)
            viewHolder.tvhitbtn = view.findViewById(R.id.hit_btn)
            viewHolder.tvdetilsbtn = view.findViewById(R.id.get_dir_btn)
            viewHolder.tvaccepttime = view.findViewById(R.id.txtacceptingtime)
            viewHolder.lyout = view.findViewById(R.id.lin1)
            viewHolder.tvtime = view.findViewById(R.id.complain_date_time)
            viewHolder.tvgetdir = view.findViewById(R.id.imgg1)


            // var uri: String? =imgpath
            viewHolder.tvTitlekt?.setText(arrcomp.get(position))
            viewHolder.tvComp_1?.setText(arrcomp1.get(position))
            viewHolder.tvComp_2?.setText(arrcomp2.get(position))
            viewHolder.tvtime?.setText(realtimearray.get(position))

            //  var hitt=hitarray.get(position).toInt()
            mykey = keylist.get(position)
            acceptstatus = acceptstatusarray.get(position)
            acceptingdate = acceptingdatearray.get(position)
            acceptingtime = acceptingtimearray.get(position)
            if (acceptstatus == "yes") {

                viewHolder.tvhitbtn?.isEnabled = false
                viewHolder.tvaccepttime?.setText("Accepting time: $acceptingdate $acceptingtime")

                //viewHolder.tvaccept?.setTextColor()
            }

            viewHolder.tvaccept?.setText("Accept status: $acceptstatus")

            Log.e("key--", mykey)

            //Log.e("hittttt1st",hitt.toString())
            getpos2 = keylist[position]

            myPref = MyPref(context!!)
            var b2 = myPref.getUserName()
            databaseReference4 = firebaseDatabase?.getReference("Mycomplains/$getpos2/count")
            Log.e("dbref3333", databaseReference3.toString())
            databaseReference4!!.orderByChild("key")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.e("Firebase Error", p0.toString())
                    }

                    override fun onDataChange(p0: DataSnapshot) {


                        var uuo = 0
                        var flag: Boolean = false
                        p0.children.forEach {

                            uuo += 1

                            // Toast.makeText(context,"enter2222",Toast.LENGTH_SHORT).show()

                            getmap = it.value as HashMap<String, String>


                            var key = getmap!!.get("key").toString()
                            var data = getmap!!.get("$key").toString()

                            Log.e("enter333", key.toString())
                            Log.e("enter444", data.toString())
                            if (data == b2) {


                                flag = true

                                //Toast.makeText(context,"alreday availabe !!",Toast.LENGTH_SHORT).show()
                            }

                        }

                        Log.e("uuuuooo", uuo.toString())
                        if (flag == true) {
                            viewHolder.tvhitbtn?.isChecked = true
                        }


                    }

                })


            //Toast.makeText(context,a.toString(),Toast.LENGTH_LONG).show()

            val child = storageReference?.child(arrimg.get(position))
            Log.e("child", child.toString())
            try {

                val localFile = File.createTempFile(imgpath, null)
                //  Log.e("localfile",localFile.toString())
                //child?.downloadUrl
                child?.getFile(localFile)?.addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    // Log.e("bbbbiitttttmmaaaappp",bitmap.toString())

                }?.addOnFailureListener { }

            } catch (e: Exception) {
                e.printStackTrace()
            }



            viewHolder.tvgetdir?.setOnClickListener {

                val lati = loclati.get(position)
                val longi = loclongi.get(position)
                val compcode = arrcomp1.get(position)

                Log.e("lati from tvgetdir", lati)
                Log.e("longi from tvgetdir", longi)

                var bundle = Bundle()
                bundle.putDouble("lati", lati.toDouble())
                bundle.putDouble("longi", longi.toDouble())
                bundle.putString("complaincode", compcode)
                //  var smap=Show_map()
                //  smap.arguments=bundle


                var intent = Intent(context, Show_Dir::class.java)
                intent.putExtras(bundle)
                startActivity(intent)

            }

            var count = 0


            viewHolder.tvhitbtn?.setOnClickListener {
                myPref = MyPref(context!!)
                // Toast.makeText(context,"entered",Toast.LENGTH_SHORT).show()
                var fkey = keylist.get(position)
                getpos = keylist.get(position)
                var b = myPref.getUserName()

                var flagc = 0


                var keyyy: String = ""
                var data: String = ""

                databaseReference3 = firebaseDatabase?.getReference("Mycomplains/$getpos/count")
                Log.e("dbref3333", databaseReference3.toString())
                databaseReference3!!.orderByChild("key")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Log.e("Firebase Error", p0.toString())
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            var flagyy = 0
                            var ex: String = ""
                            var ex2: String = ""
                            Log.e("bbbb", b.toString())
                            var flag: Boolean = false
                            p0.children.forEach {

                                //   Toast.makeText(context,flagyy.toString(),Toast.LENGTH_SHORT).show()

                                getmap = it.value as HashMap<String, String>

                                Log.e("geeetttmaaapp", getmap.toString())



                                keyyy = getmap!!.get("key").toString()
                                data = getmap!!.get("$keyyy").toString()


                                // Log.e("enter333",key.toString())
                                // Log.e("enter444",data.toString())
                                if (data == b) {
                                    //Toast.makeText(context!!,flagyy.toString(),Toast.LENGTH_LONG).show()
                                    //  Log.e("newwwkeeyy",newkey.toString())
                                    ex = data
                                    ex2 = keyyy

                                    Log.e("exxx", ex.toString())
                                    Log.e("exxx2", ex2.toString())
                                    flag = true

                                    //  return

                                    //Toast.makeText(context,"alreday availabe !!",Toast.LENGTH_SHORT).show()
                                }


                            }

                            Log.e("fllllaaaaggg", flagyy.toString())
                            if (flag == true) {


                                //  if (data==b) {

                                // flagyy-=1
                                // databaseReference2?.child(getpos)?.child("count")?.removeValue()
                                databaseReference3?.child(ex2!!)?.removeValue()
                                databaseReference!!.child(fkey).child("hit").setValue(p0.childrenCount - 1)
                                //}

                                Log.e("mistrykey", ex2.toString())
                                // databaseReference?.child(getpos!!)?.child("hit")?.setValue(hit22-1)

                                // Log.e("count-exist",count.toString())
                                //viewHolder.tvhitbtn?.isChecked=true
                                // Toast.makeText(context,"exist data",Toast.LENGTH_SHORT).show()
                            } else {
                                // flagyy+=1

                                var newmap = HashMap<String, String>()

                                Log.e("getposkey", getpos)
                                myPref = MyPref(context!!)

                                Log.e("bbb", b)

                                databaseReference2 = firebaseDatabase?.getReference("Mycomplains/$getpos/count")
                                Log.e("dbref2", databaseReference2.toString())

                                newkey = databaseReference2?.push()?.key.toString()
                                //Log.e("newkey",newkey)


                                Log.e("count-add", count.toString())

                                newmap.put("key", newkey.toString())
                                newmap.put("$newkey", b.toString())
                                // newmap.put("flag",count.toString())
                                // Log.e("newmappp", flagc.toString())
                                //databaseReference?.child(getpos!!)?.child("hit")?.setValue(hit22+1)
                                databaseReference2?.child(newkey!!)?.setValue(newmap)

                                //  Toast.makeText(context, "data added", Toast.LENGTH_SHORT).show()
                                databaseReference!!.child(fkey).child("hit").setValue(p0.childrenCount + 1)
                            }

                        }

                    })
                // countlike(getpos!!)
            }

            viewHolder.tvdetilsbtn?.setOnClickListener {
                var bundle = Bundle()
                bundle.putString("fullname", arrcomp.get(position))
                bundle.putString("comp_c1", arrcomp1.get(position))
                bundle.putString("comp_c2", arrcomp2.get(position))
                bundle.putString("imgpath", arrimg.get(position))
                bundle.putString("videopath", arrvdo.get(position))
                bundle.putString("zone", zonearray.get(position))
                bundle.putString("ward", wardarray.get(position))
                bundle.putString("compdes", compdesarray.get(position))
                var intent = Intent(context, Complain_Fulldescription::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            return view

        }

        override fun getItem(position: Int): Any {
            return arrcomp.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return arrcomp.size
        }

    }

    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var databaseReference2: DatabaseReference? = null
    var databaseReference3: DatabaseReference? = null
    var databaseReference4: DatabaseReference? = null
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    var storageReference2: StorageReference? = null

    var map: HashMap<String, String>? = null
    var getmap: HashMap<String, String>? = null
    var getpos2 = ""
    var key: String? = null
    var newkey: String? = null
    var username: String? = null

    var firstname: String? = null
    var t: String? = null
    var lastname: String? = null
    var imgpath: String? = null
    var vdopath: String? = null
    var fullname: String? = null
    var comp_c1: String? = null
    var comp_c2: String? = null
   var newlati: Double? = 0.0
    var newlonti: Double? = 0.0
    var latifire: Double? = 0.0
    var longifire: Double? = 0.0
    var mykey = ""
    var hit: String = ""
    var acceptstatus = ""
    var getpos: String? = null
    var ward = ""
    var zone = ""
    var compdes = ""
    var acceptingtime = ""
    var acceptingdate = ""

    var xyz = ArrayList<String>()
    var xyz1 = ArrayList<String>()
    var xyz2 = ArrayList<String>()
    var imgarray = ArrayList<String>()
    var vdoarray = ArrayList<String>()
    var locarray = ArrayList<String>()
    var keylist = ArrayList<String>()
    var locarray2 = ArrayList<String>()
    var realtime = ArrayList<String>()
    var zonearray = ArrayList<String>()
    var wardarray = ArrayList<String>()
    var compdesarray = ArrayList<String>()
    var acceptingdatearray = ArrayList<String>()
    var acceptingtimearray = ArrayList<String>()


    var acceptstatusarray = ArrayList<String>()

    lateinit var myPref: MyPref


    var receiver222 = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            var bundle = intent?.extras
            Log.e("onreceive", "entered in feedfrg")
            if (bundle != null) {
                Log.e("Bundle Fragment Data", bundle.toString())
                newlati = bundle.getString("latitude").toDouble()
                newlonti = bundle.getString("longitude").toDouble()
                Log.e("newlatifromgooglenearkt", newlati.toString())
                Log.e("newlongifrmgooglenearkt", newlonti.toString())

                Toast.makeText(context, "current location is fetched !!", Toast.LENGTH_SHORT).show()


            }

        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feedfragment, container, false)
    }
    override fun onResume() {
        super.onResume()
        var intent = Intent(activity, Mylocation::class.java)
        activity?.startService(intent)
        activity?.registerReceiver(receiver222, IntentFilter("LOC_RECEIVED 2"))
    }

    override fun onPause() {
        super.onPause()
        activity?.unregisterReceiver(receiver222)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swiperefresh.setOnRefreshListener(this)
        xyz.clear()
        xyz1.clear()
        xyz2.clear()
        keylist.clear()
        imgarray.clear()
        vdoarray.clear()
        locarray.clear()
        locarray2.clear()
        realtime.clear()
        acceptstatusarray.clear()
        zonearray.clear()
        wardarray.clear()
        compdesarray.clear()
        acceptingtimearray.clear()
        acceptingdatearray.clear()
        storage = FirebaseStorage.getInstance()
        storageReference = storage?.getReferenceFromUrl("gs://mypublicservice-98437.appspot.com/complain_images/")
        storageReference2 = storage?.getReferenceFromUrl("gs://mypublicservice-98437.appspot.com/complain_videos/")
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("Mycomplains")
        databaseReference!!.orderByChild("Mycomplains")

            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {

                    p0.children.forEach {
                        map = it.value as HashMap<String, String>

                        firstname = map!!.get("fname").toString()
                        lastname = map!!.get("lname").toString()
                        imgpath = map!!.get("imagepath").toString()
                        vdopath = map!!.get("videopath").toString()
                        fullname = map!!.get("fullname").toString()
                        comp_c1 = map!!.get("complaincatogary").toString()
                        comp_c2 = map!!.get("complaincode").toString()
                        latifire = map?.get("latitude")?.toDouble()
                        longifire = map?.get("longtitude")?.toDouble()
                        key = map!!.get("key").toString()
                        t = map!!.get("realtime").toString()
                        hit = map!!.get("hit").toString()
                        acceptstatus = map!!.get("accepted").toString()
                        ward = map!!.get("ward").toString()
                        zone = map!!.get("zone").toString()
                        compdes = map!!.get("complain_des").toString()
                        acceptingdate = map!!.get("acceptingdate").toString()
                        acceptingtime = map!!.get("acceptingtime").toString()


                        if (newlati != 0.0 && newlonti != 0.0) {
                            val Radius = 6371// radius of earth in Km
                            val lat1 = newlati
                            Log.e("callat1", lat1.toString())
                            val lat2 = latifire!!
                            Log.e("callat2", lat2.toString())
                            val lon1 = newlonti
                            Log.e("callon1", lon1.toString())
                            val lon2 = longifire!!
                            Log.e("callon2", lon2.toString())
                            val dLat = Math.toRadians(lat2 - lat1!!)
                            Log.e("dLat", dLat.toString())
                            val dLon = Math.toRadians(lon2 - lon1!!)
                            Log.e("dLon", dLat.toString())
                            val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + (Math.cos(Math.toRadians(lat1))
                                    * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                                    * Math.sin(dLon / 2))
                            Log.e("aaa", a.toString())
                            val c = 2 * Math.asin(Math.sqrt(a))
                            Log.e("ccc", c.toString())
                            val valueResult = Radius * c
                            Log.e("valres", valueResult.toString())
                            val km = valueResult / 1
                            val newFormat = DecimalFormat("####")
                            val kmInDec = Integer.valueOf(newFormat.format(km))
                            val meter = valueResult * 1000
                            val meterInDec = Integer.valueOf(newFormat.format(meter))

                            Log.e("km distance", km.toString())
                            Log.e("Meter distance", meterInDec.toString())

                            if (meterInDec <= 400 && map!!.get("solved").toString().equals("no")) {
                                xyz.add(fullname!!)
                                xyz1.add(comp_c1!!)
                                xyz2.add(comp_c2!!)
                                keylist.add(key!!)
                                imgarray.add(imgpath!!)
                                vdoarray.add(vdopath!!)
                                locarray.add(latifire.toString())
                                locarray2.add(longifire.toString())
                                realtime.add(t.toString())
                                acceptstatusarray.add(acceptstatus)
                                zonearray.add(zone)
                                wardarray.add(ward)
                                compdesarray.add(compdes)
                                acceptingdatearray.add(acceptingdate)
                                acceptingtimearray.add(acceptingtime)
                            }

                        }
                        getbaseview()
                    }
                }
            })
    }

    lateinit var imgadapter: abc
    fun getbaseview() {
        var imgadapter = abc(
            xyz,
            xyz1,
            xyz2,
            imgarray,
            vdoarray,
            locarray,
            locarray2,
            realtime,
            acceptstatusarray,
            wardarray,
            zonearray,
            compdesarray,
            acceptingdatearray,
            acceptingtimearray
        )
        listv1.adapter = imgadapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notification()
    }

    fun notification() {
      Log.e("receiver::", "call")
        var intentcall: Intent = Intent(activity!!.applicationContext, NotificationBroadcast::class.java)
        var pendingintent: PendingIntent =
            PendingIntent.getBroadcast(activity!!.applicationContext, 2244, intentcall, 0)
        var alarmManager: AlarmManager? = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var cal: Calendar = Calendar.getInstance()
        alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, 60000, pendingintent)


    }
}
