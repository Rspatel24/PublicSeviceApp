package com.example.contr.publicservice.Services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.contr.publicservice.Set_ward_zone
import java.util.*

class Mylocation:Service() {

    lateinit var context: Context
    lateinit var listener: Mylistner
    lateinit var locationManager: LocationManager
    override fun onCreate() {
        super.onCreate()
        context=this
    }

    @SuppressLint("MissingPermission")
    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        Log.e("Service","Started")
        listener= Mylistner()
        locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
            60000,
            10.0F,
            listener)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
            60000,
            10.0F,
            listener)
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    inner class Mylistner:LocationListener
    {
        //var gmf=GoogleMapFragment()

        var lati=0.0
        var longti=0.0

        var ex=0
        override fun onLocationChanged(location: Location?) {
            if (location!=null)
            {

                 lati=location?.latitude
                 longti=location?.longitude



                /*int maxLines = address.get(0).getMaxAddressLineIndex();
                for (int i=0; i<maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");*/

                Log.e("latitude from Myloc",location?.latitude.toString())
                Log.e("longitude from Myloc",location?.longitude.toString())
                Log.e("speed from Myloc",location.speed.toString())


                var intent = Intent("LOC_RECEIVED")
                var bundle = Bundle()
                bundle.putString("latitude",location?.latitude.toString())
                bundle.putString("longitude",location?.longitude.toString())
                bundle.putString("speed",location?.speed.toString())
                intent.putExtras(bundle)

                var intent2 = Intent("LOC_RECEIVED 2")
                var bundle2 = Bundle()
                bundle2.putString("latitude",location?.latitude.toString())
                bundle2.putString("longitude",location?.longitude.toString())
                bundle2.putString("speed",location?.speed.toString())
                intent2.putExtras(bundle2)

                context.sendBroadcast(intent)
                context.sendBroadcast(intent2)


              //  gmf.getlocation(lati,longti)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }

        override fun onProviderEnabled(provider: String?) {

        }

        override fun onProviderDisabled(provider: String?) {

        }

    }
}