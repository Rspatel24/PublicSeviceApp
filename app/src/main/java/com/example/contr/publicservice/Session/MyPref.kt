package com.example.contr.publicservice.Session

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log

class MyPref {

    lateinit var sharedPrefrences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    var arraydata= arrayListOf<String>()
    val KEY_USER_NAME ="username"
    val KEY_USER_NAME2 ="firstname"
    val KEY_USER_NAME3 ="lastname"
    val KEY_USER_NAME4="password"
    val KEY_USER_NAME5="latitude"
    val KEY_USER_NAME6="longitude"
    val KEY_USER_PINCODE="pincode"

    var latitude1:String?=null
    var longitude1:String?=null

    constructor(context : Context)
    {
        sharedPrefrences= context.getSharedPreferences("mypref",Context.MODE_PRIVATE)
        editor=sharedPrefrences.edit()
    }

    fun setUserName(userName : String,firstName:String,lastName:String,password:String)
    {
        editor.putString(KEY_USER_NAME,userName)
        editor.putString(KEY_USER_NAME2,firstName)
        editor.putString(KEY_USER_NAME3,lastName)
        editor.putString(KEY_USER_NAME4,password)
        editor.commit()
    }

    fun setloc(latitude:Double,longitutde:Double)
    {
        Log.e("get latitude1",latitude.toString())
        Log.e("get longitude",longitutde.toString())
        editor.putString(KEY_USER_NAME5,latitude.toString())
        editor.putString(KEY_USER_NAME6,longitutde.toString())
        editor.commit()
    }
    fun setpincode(pincode:String){

        editor.putString(KEY_USER_PINCODE,pincode)
        editor.commit()

    }


    fun getUserName() : String?
    {
        return sharedPrefrences.getString(KEY_USER_NAME,null)
    }
    fun getpincode():String?{

        return sharedPrefrences.getString(KEY_USER_PINCODE,null)
    }

    fun getfirstname() :String?
    {
        return sharedPrefrences.getString(KEY_USER_NAME2,null)
    }

    fun getlastname() :String?
    {
        return sharedPrefrences.getString(KEY_USER_NAME3,null)
    }
    fun getpassword() :String?
    {
        return sharedPrefrences.getString(KEY_USER_NAME4,null)

    }
    fun getlati() : String?
    {
        Log.e("getlati",sharedPrefrences.getString(KEY_USER_NAME5,null).toString())
        return sharedPrefrences.getString(KEY_USER_NAME5,null)
    }
    fun getlongi():String?
    {
        Log.e("getlongi",sharedPrefrences.getString(KEY_USER_NAME6,null).toString())
        return sharedPrefrences.getString(KEY_USER_NAME6,null)
    }

    fun Logout()
    {
        editor.clear()
        editor.commit()
    }


}