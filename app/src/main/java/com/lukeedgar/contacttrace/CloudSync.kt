package com.lukeedgar.contacttrace

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class PostiveIDUploader(private val context: Context) {

    fun send(postiveIDs : List<ExposureId>) {
        val callAPI = CallAPI()
        val url =  "https://us-central1-cancerresearchsystem.cloudfunctions.net/add-postive-covid-id"
        val jsonArray = JSONArray()
        jsonArray.put(postiveIDs)
        val jsonBody = jsonArray.toJSONObject(jsonArray)
        val response = callAPI.doInBackground(context, url, jsonBody)
        Toast.makeText(context, response, Toast.LENGTH_LONG).show()
    }
}

open class CallAPI  {

    public fun doInBackground(context: Context, url: String, jsonBody : JSONObject) : String {
        var finalResponse = ""
        try {
            val requestQueue = Volley.newRequestQueue(context)
            val mRequestBody = jsonBody.toString()
            val stringRequest: StringRequest = object : StringRequest(
                Method.POST,
                url,
                Response.Listener{
                        response ->
                    Log.i("LOG_VOLLEY", response)
                },
                Response.ErrorListener { error -> Log.e("LOG_VOLLEY", error.toString()) }) {
                override fun getBodyContentType(): String = "application/json; charset=utf-8"

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray =
                    mRequestBody.toByteArray(charset("utf-8"))

                override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
                    var responseString = ""
                    responseString = response.statusCode.toString()
                    return Response.success(
                        responseString,
                        HttpHeaderParser.parseCacheHeaders(response)
                    )
                }

                override fun hasHadResponseDelivered(): Boolean {
                    return super.hasHadResponseDelivered()
                }

                override fun deliverResponse(response: String?) {
                    super.deliverResponse(response)
                        finalResponse = response!!
                }
            }
            requestQueue.add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return finalResponse
    }
}