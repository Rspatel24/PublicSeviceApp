package com.example.contr.publicservice.Fragments


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.contr.publicservice.Get_Direction
import com.example.contr.publicservice.Services.Mylocation

import com.example.contr.publicservice.R
import com.example.contr.publicservice.Session.MyPref
import com.example.contr.publicservice.Set_ward_zone
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class GoogleMapFragment : Fragment(), OnMapReadyCallback {
     var lati:Double = 0.0
    var longti:Double=0.0

    lateinit var myPref: MyPref

    lateinit var listener: Mylocation.Mylistner
    lateinit var locationManager: LocationManager

    var newlati:Double=0.0
    var newlonti:Double=0.0


    var receiver = object :BroadcastReceiver()
    {
        override fun onReceive(context: Context?, intent: Intent?) {
            var bundle = intent?.extras
            Log.e("onreceive","entered")
            if(bundle!=null && map!=null)
            {
                Log.e("Bundle Fragment Data",bundle.toString())

                map.clear()
                newlati=bundle.getString("latitude").toDouble()
                newlonti=bundle.getString("longitude").toDouble()
                Log.e("newlatifromgoogle",newlati.toString())
                Log.e("newlongifromgoogle",newlonti.toString())


                var geocoder= Geocoder(context, Locale.getDefault())
                var builder = StringBuilder()
                var addr=geocoder.getFromLocation(newlati,newlonti,1)
                Log.e("addddrrrrrreeeesss",addr.toString())


                var pincode=addr[0].postalCode
               Log.e("Location::",pincode)
                myPref.setloc(newlati,newlonti)
                myPref.setpincode(pincode)

                lati=newlati
                longti=newlonti
                Log.e("latifromgoogle",lati.toString())
                Log.e("longifromgoogle",longti.toString())
                var hj=LatLng(bundle.getString("latitude").toDouble(),bundle.getString("longitude").toDouble())
                var mark=MarkerOptions()
                mark.position(hj).title("You are here !!")
                map.addMarker(mark)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(hj,15.2f))


            }

        }

    }

    override fun onResume() {
        super.onResume()
        var intent=Intent(activity,Mylocation::class.java)
        activity?.startService(intent)
        activity?.registerReceiver(receiver, IntentFilter("LOC_RECEIVED"))
    }

    override fun onPause() {
        super.onPause()
        activity?.unregisterReceiver(receiver)
    }

    lateinit var map:GoogleMap
    override fun onMapReady(p0: GoogleMap?) {
            map= p0!!
       // Log.e("GoogleMapfragment",lati1.toString())
       // Log.e("GoogleMapfragment",longti1.toString())
        var hj=LatLng(newlati,newlonti)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var v:View= inflater.inflate(R.layout.fragment_google_map, container, false)
       return  v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myPref= MyPref(this!!.context!!)
        var mapFragment:SupportMapFragment= childFragmentManager?.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

}
