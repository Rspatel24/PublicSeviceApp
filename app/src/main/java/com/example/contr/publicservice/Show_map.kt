package com.example.contr.publicservice

import android.content.Context
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class Show_map : Fragment(), OnMapReadyCallback {

    lateinit var map: GoogleMap

    var newlati = 0.0
    var newlonti = 0.0
    var compcode:String?=null

    override fun onMapReady(p0: GoogleMap?) {
        map = p0!!
        var bundle = arguments
        if (bundle != null) {
            map.clear()
            newlati = bundle.getDouble("lati")
            newlonti = bundle.getDouble("longi")
            compcode=bundle.getString("complaincode")
            Log.e("lati from showmap", newlati.toString())
            Log.e("longi from showmap", newlonti.toString())

        }


        var hj = LatLng(newlati, newlonti)
        var mark = MarkerOptions()
        mark.position(hj).title("$compcode")
        map.addMarker(mark)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(hj, 15.2f))


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var mapFragment: SupportMapFragment = childFragmentManager?.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }
}




