package com.example.nanoleafdiy

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


class ApiService { companion object {
    interface API {
        @GET("")
        fun health(): Call<JsonElement>

        @GET("network")
        fun getNetworkTopology(): Call<JsonElement>

        @POST("panels/color")
        fun setColor(@Body body: JsonElement): Call<JsonElement>
    }

    // Static IP because I couldn't get Android to recognize mDNS :(
    private const val API_URL: String = "http://192.168.0.231"

    private lateinit var api: API
    private lateinit var toastContext: Context

    fun init(context: Context){
        api = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)
        toastContext = context
    }

    fun health(resolve: (Boolean) -> Unit){
        api.health().enqueue(ResponseCallback(fun(res: JsonElement) {
            resolve(true)
        }))
    }

    fun getNetworkTopology(resolve: (String) -> Unit){
        api.getNetworkTopology().enqueue(ResponseCallback(fun(res: JsonElement) {
            resolve(res.toString().substring(1,res.toString().length-1))
        }))
    }

    fun setColor(panel: Panel){
        val body = "{ \"directions\": \"${panel.directions}\", \"r\": \"${panel.r}\", \"g\": \"${panel.g}\", \"b\": \"${panel.b}\" }"

        api.setColor(JsonParser().parse(body)).enqueue(ResponseCallback(fun(_: JsonElement) {}))
    }

    class ResponseCallback constructor(private val onRes: (JsonElement)->Unit): Callback<JsonElement>{
        override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
            if(response.isSuccessful)
                onRes(response.body()!!.asJsonObject["data"])
            else {
                println("Error calling light panel API %s".format(call.request().url()))
                println(response.body()!!.asJsonObject["err"])
            }
        }
        override fun onFailure(call: Call<JsonElement>, t: Throwable) {
            t.printStackTrace()
            Toast.makeText(toastContext, t.message!!, Toast.LENGTH_LONG).show()
        }
    }
}}
