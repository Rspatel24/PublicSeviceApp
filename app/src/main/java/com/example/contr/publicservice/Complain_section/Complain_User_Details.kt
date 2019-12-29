package com.example.contr.publicservice.Complain_section

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.contr.publicservice.HighNotificationdetail
import com.example.contr.publicservice.MoreInfo
import com.example.contr.publicservice.R
import com.example.contr.publicservice.Session.MyPref
import com.example.contr.publicservice.Show_Dir
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_complain__details.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.complain_description.*
import kotlinx.android.synthetic.main.feed.*
import kotlinx.android.synthetic.main.fragment_feedfragment.*
import java.io.File

class Complain_User_Details : AppCompatActivity(){

    lateinit var myPref: MyPref
    inner class abc2(context: Context,
                     internal var arrcomp: ArrayList<String>,
                     internal var arrcomp1: ArrayList<String>,
                     internal var arrcomp2: ArrayList<String>,
                     internal var arrimg:ArrayList<String>,
                     internal var vdoarray:ArrayList<String>,
                     internal var loclati: ArrayList<String>,
                     internal var loclongi: ArrayList<String>,
                     internal var realtime:ArrayList<String>,
                     internal var acceptstatusarray:ArrayList<String>,
                     internal var zonearray:ArrayList<String>,
                     internal var wardarray:ArrayList<String>,
                     internal var compdesarray:ArrayList<String>,
                     internal var keylist:ArrayList<String>,
                     internal var acceptingdatearray:ArrayList<String>,
                     internal var acceptingtimearray:ArrayList<String>
                     ):BaseAdapter()
    {
        var layoutInflater: LayoutInflater

        init{
            layoutInflater=context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        }
        inner class ViewHolder
        {
            var imgktt: ImageView?=null
            var tvTitlekt: TextView?=null
            var tvComp_1: TextView?=null
            var tvComp_2: TextView?=null
            var tvaccept: TextView?=null
            var tvdatetime: TextView?=null
            var tvempbtn: Button? = null
            var lyout: LinearLayout?=null
            var tvbtn: Button?=null
            var tvaccepttime:TextView?=null


        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var viewHolder=ViewHolder()
            var view=layoutInflater.inflate(R.layout.complain_description,null)
            viewHolder.imgktt=view.findViewById(R.id.imgg1_cd)
            viewHolder.tvTitlekt=view.findViewById(R.id.feedusername1_cd)
            viewHolder.tvComp_1=view.findViewById(R.id.complain_cat_1_cd)
            viewHolder.tvComp_2=view.findViewById(R.id.complain_cat_2_cd)
            viewHolder.tvempbtn = view.findViewById(R.id.empdetail)
            viewHolder.tvaccepttime=view.findViewById(R.id.txtacceptingtime)
            viewHolder.tvbtn=view.findViewById(R.id.get_detail)
            viewHolder.lyout=view.findViewById(R.id.comp_des_1)
            viewHolder.tvaccept=view.findViewById(R.id.acceptstatus)
            viewHolder.tvdatetime=view.findViewById(R.id.complain_date_time)



            /*hit_btn_cd.setOnClickListener {



            }*/
            // viewHolder.gg.get(position)

            // var uri: String? =imgpath
            viewHolder.tvTitlekt?.setText(arrcomp.get(position))
            viewHolder.tvComp_1?.setText(arrcomp1.get(position))
            viewHolder.tvComp_2?.setText(arrcomp2.get(position))
            viewHolder.tvdatetime?.setText(realtime.get(position))
            mykey = keylist.get(position)
            acceptstatus=acceptstatusarray.get(position)
            acceptingdate=acceptingdatearray.get(position)
            acceptingtime=acceptingtimearray.get(position)
            if(acceptstatus=="no"){
                viewHolder.tvempbtn?.isEnabled=false

            }
            if(acceptstatus=="yes"){

                viewHolder.tvaccepttime?.setText("Accepting time: $acceptingdate $acceptingtime")
            }



            viewHolder.tvaccept?.setText("Accept status: $acceptstatus")
            department=arrcomp1.get(position)
            databaseReference3=firebaseDatabase?.getReference("MyEmployee")
            databaseReference3!!.orderByChild("MyEmployee")
                .addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        p0.children.forEach {
                            map2 = it.value as HashMap<String, String>
                            empdepartment= map2!!.get("department").toString()
                            if(department==empdepartment){
                                ename=map2!!.get("name").toString()
                                emobno=map2!!.get("mobno").toString()
                                empdepartment=map2!!.get("department").toString()




                            }



                        }
                    }


                })
            //Toast.makeText(context,a.toString(),Toast.LENGTH_LONG).show()
            viewHolder.imgktt?.setOnClickListener {

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


                var intent = Intent(this@Complain_User_Details, Show_Dir::class.java)
                intent.putExtras(bundle)
                startActivity(intent)

            }
            viewHolder.tvempbtn?.setOnClickListener {

                var bundle= Bundle()

                bundle.putString("ename1",ename)
                bundle.putString("emobno1",emobno)
                bundle.putString("empdepartment1",department)
                var intent=Intent(this@Complain_User_Details ,MoreInfo::class.java)
                intent.putExtras(bundle)
                startActivity(intent)

            }
            viewHolder.tvbtn?.setOnClickListener {
                var bundle = Bundle()
                bundle.putString("fullname", arrcomp.get(position))
                bundle.putString("comp_c1", arrcomp1.get(position))
                bundle.putString("comp_c2", arrcomp2.get(position))
                bundle.putString("imgpath", arrimg.get(position))
                bundle.putString("videopath", vdoarray.get(position))
                bundle.putString("zone",zonearray.get(position))
                bundle.putString("ward",wardarray.get(position))
                bundle.putString("compdes",compdesarray.get(position))
                var intent = Intent(this@Complain_User_Details, Complain_Fulldescription::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            val child = storageReference?.child(arrimg.get(position))
            Log.e("child",child.toString())
            try {

                val localFile = File.createTempFile(imgpath,null)
                Log.e("localfile",localFile.toString())
                //child?.downloadUrl
                child?.getFile(localFile)?.addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    Log.e("bbbbiitttttmmaaaappp",bitmap.toString())

                }?.addOnFailureListener { }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return view

        }

        override fun getItem(position: Int): Any {
            //Toast.makeText(this@Complain_User_Details,arrcomp.get(position),Toast.LENGTH_LONG).show()

            return arrcomp.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return arrcomp.size
        }

    }
    var ward=""
    var zone=""
    var compdes=""
var department=""
    var ename=""
    var emobno=""
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var storage: FirebaseStorage?=null
    var storageReference: StorageReference?=null
    var storageReference2: StorageReference?=null
    var map:HashMap<String,String>?=null
    var map1:HashMap<String,String>?=null
    var key=""
    var databaseReference3: DatabaseReference? = null
    var firstname: String?=null
    var lastname:String?=null
    var imgpath:String?=null
    var vdopath:String?=null
    var fullname:String?=null
    var comp_c1:String?=null
    var comp_c2:String?=null
    var empdepartment=""
    var hit: String = ""
    var mykey = ""
    var t: String? = null
    var acceptstatus=""
    var acceptingtime=""
    var acceptingdate=""

    var latifire: Double? = 0.0
    var longifire: Double? = 0.0
    var xyz=ArrayList<String>()
    var xyz1=ArrayList<String>()
    var xyz2=ArrayList<String>()

    var vdoarray = ArrayList<String>()
    var locarray = ArrayList<String>()
    var keylist = ArrayList<String>()
    var locarray2 = ArrayList<String>()
    var realtime = ArrayList<String>()
    var zonearray = ArrayList<String>()
    var wardarray = ArrayList<String>()
    var compdesarray = ArrayList<String>()
    var acceptingdatearray=ArrayList<String>()
    var acceptingtimearray=ArrayList<String>()
    var map2: HashMap<String, String>? = null
    var highname=""
    var highempid=""
    var highempdepartment=""
    var hitarray = ArrayList<String>()


    var acceptstatusarray = ArrayList<String>()


    var imgarray=ArrayList<String>()

    lateinit var pDialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complain__details)


        var username:String?=null
        myPref= MyPref(this)

        if (myPref.getUserName()!=null)
        {
            username=myPref.getUserName()
        }
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
        storage= FirebaseStorage.getInstance()
        storageReference = storage?.getReferenceFromUrl("gs://mypublicservice-98437.appspot.com/complain_images/")
        storageReference2 = storage?.getReferenceFromUrl("gs://mypublicservice-98437.appspot.com/complain_videos/")

        storage=FirebaseStorage.getInstance()
        firebaseDatabase= FirebaseDatabase.getInstance()
        databaseReference=firebaseDatabase?.getReference("Mycomplains")
        databaseReference!!.orderByChild("eid").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {

                    p0.children.forEach{

                        map = it.value as HashMap<String, String>

                        if (map!!.get("eid").toString().equals(username))
                        {
                            firstname=map!!.get("fname").toString()
                            lastname=map!!.get("lname").toString()
                            imgpath=map!!.get("imagepath").toString()
                            vdopath=map!!.get("videopath").toString()
                            fullname=map!!.get("fullname").toString()
                            comp_c1=map!!.get("complaincatogary").toString()
                            comp_c2=map!!.get("complaincode").toString()
                            key = map!!.get("key").toString()
                            t = map!!.get("realtime").toString()
                            hit = map!!.get("hit").toString()
                            latifire = map?.get("latitude")?.toDouble()
                            longifire = map?.get("longtitude")?.toDouble()
                            acceptstatus=map!!.get("accepted").toString()
                            ward=map!!.get("ward").toString()
                            zone=map!!.get("zone").toString()
                            compdes=map!!.get("complain_des").toString()
                            acceptingdate=map!!.get("acceptingdate").toString()
                            acceptingtime=map!!.get("acceptingtime").toString()
                            Log.e("firstnameofcomplain_des",firstname)
                            Log.e("lastnameofcomplain_des",lastname)
                            Log.e("imgpathofcomplain_des",imgpath)
                            Log.e("vdopathofcomplain_des",vdopath)
                        }
                        // Log.e("ex",ex.toString())
                        // Log.e("ex2",ex2.toString())
                        xyz.add(fullname!!)
                        xyz1.add(comp_c1!!)
                        xyz2.add(comp_c2!!)
                        keylist.add(key)
                        imgarray.add(imgpath!!)
                        vdoarray.add(vdopath!!)
                        realtime.add(t.toString())
                        acceptstatusarray.add(acceptstatus)
                        zonearray.add(zone)
                        wardarray.add(ward)
                        compdesarray.add(compdes)
                        locarray.add(latifire.toString())
                        locarray2.add(longifire.toString())
                       acceptingdatearray.add(acceptingdate)
                        acceptingtimearray.add(acceptingtime)

                        //  arraycomplain.add(map!!)
                        // pDialog.setMessage("Please wait...")
                        // pDialog.setCancelable(false)
                        getbaseview()
                        //  feedusername1.text = firstname+lastname
                    }
                }
            })

    }

    fun getbaseview()
    {
        var imgadapter=abc2(this,xyz,xyz1,xyz2,imgarray,vdoarray, locarray, locarray2,realtime,acceptstatusarray,zonearray,wardarray,compdesarray,keylist,acceptingdatearray,acceptingtimearray)
        listv2.adapter=imgadapter
    }

}

