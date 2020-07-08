package com.example.nanoleafdiy

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


class ApiService { companion object {
    interface API {
        @GET("")
        fun health(): Call<JsonElement>

        @GET("network")
        fun getNetworkTopology(): Call<JsonElement>
    }

    // Static IP because I couldn't get Android to recognize mDNS :(
    private const val API_URL: String = "http://192.168.0.231"

    var initialized: Boolean = false
    lateinit var api: API

    private fun init(){
        api = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)
        initialized = true
    }

    fun health(resolve: (Boolean) -> Unit){
        if(!initialized) init()
        api.health().enqueue(ResponseCallback(
            fun(res: JsonElement) { resolve(true) },
            null
        ))
    }

    fun getNetworkTopology(resolve: (String) -> Unit){
        if(!initialized) init()
        api.getNetworkTopology().enqueue(ResponseCallback(
            fun(res: JsonElement) { resolve(res.toString()) },
            null
        ))
    }

    class ResponseCallback constructor(private val onRes: (JsonElement)->Unit, private val onErr: ((String)->Unit)?): Callback<JsonElement>{
        override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
            onRes(response.body()!!.asJsonObject["data"])
        }
        override fun onFailure(call: Call<JsonElement>, t: Throwable) {
            if(onErr != null)   onErr.invoke(t.message!!)
            else                println(t.message!!)
        }
    }
}}
