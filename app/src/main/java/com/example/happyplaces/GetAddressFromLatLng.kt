package com.example.happyplaces

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.lang.StringBuilder
import java.util.*

class GetAddressFromLatLng(context: Context, private val latitude: Double, private val longitude: Double) : AsyncTask<Void, String, String>() {

    private val geocoder : Geocoder = Geocoder(context, Locale.getDefault())

    private lateinit var mAddressListener: AddressListener

    override fun doInBackground(vararg p0: Void?): String {
        try {
            val addressList: List<Address>? = geocoder.getFromLocation(latitude,longitude,1)
            if (addressList != null&& addressList.isNotEmpty()){
                val address: Address = addressList[0]
                val sb = StringBuilder()
                for (i in 0..address.maxAddressLineIndex){
                    sb.append(address.getAddressLine(i)).append(",")
                }
                sb.deleteCharAt(sb.length -1)
                return sb.toString()
            }
        } catch (e: IOException){
            Log.e("HappyPLaces", "Unable Connect To Geocoder")
        }
        return ""
    }

    fun setAddressListener(addressListener: AddressListener){
        mAddressListener = addressListener
    }

    override fun onPostExecute(resultString: String?) {
        if (resultString == null){
            mAddressListener.onError()
        }else{
            mAddressListener.onAddressFound(resultString)
        }
        super.onPostExecute(resultString)
    }

    interface AddressListener {
        fun onError()

        fun onAddressFound(address: String)

    }
    fun getAddress(){
        execute()
    }

}
