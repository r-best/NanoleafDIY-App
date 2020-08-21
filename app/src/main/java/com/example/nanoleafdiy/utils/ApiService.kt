package com.example.nanoleafdiy.utils

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


class ApiService { companion object {
    interface API {
        @GET("") fun health(): Call<JsonElement>

        /** Obtain the cached panel network configuration on the controller */
        @GET("network") fun getNetworkTopology(): Call<JsonElement>

        /** Obtain the current panel network configuration, forcing the controller to recompute the network topology */
        @GET("network/refresh") fun getNetworkTopologyWithRefresh(): Call<JsonElement>

        /** Retrieve the current state of a panel, including its active mode and any settings related to that mode */
        @POST("panels/state") fun getPanelState(@Body body: JsonElement): Call<JsonElement>

        /** Sets the active lighting mode of a given panel (solid color, gradient, rainbow, etc..) */
        @POST("panels/mode") fun setMode(@Body body: JsonElement): Call<JsonElement>

        /** Set the stored solid color state of a given panel */
        @POST("panels/color") fun setColor(@Body body: JsonElement): Call<JsonElement>

        /** Set the stored gradient state of a given panel */
        @POST("panels/customgradient") fun setGradient(@Body body: JsonElement): Call<JsonElement>
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

    fun getNetworkTopology(refresh: Boolean, resolve: (String) -> Unit){
        val op = if(refresh) api::getNetworkTopologyWithRefresh else api::getNetworkTopology
        op().enqueue(ResponseCallback(fun(res: JsonElement){
            resolve(res.toString().substring(1, res.toString().length - 1))
        }))
    }

    fun getPanelState(panel: Panel, cb: (() -> Unit)?){
        val body = "{ \"directions\": \"%s\" }".format(panel.directions)
        api.getPanelState(JsonParser().parse(body)).enqueue(ResponseCallback(fun(res: JsonElement){
            val state = res.toString().substring(1, res.toString().length - 1)
            panel.mode = state.substring(0, 1).toInt()

            when(panel.mode){
                0 -> {
                    panel.r = state.substring(1, 3).toInt(16)
                    panel.g = state.substring(3, 5).toInt(16)
                    panel.b = state.substring(5, 7).toInt(16)
                }
                1 -> {
                    panel.gradientSteps = mutableListOf()
                    for(i in 2 until state.length-1 step 10){
                        panel.gradientSteps.add(GradientStep(
                            state.substring(i, i+2).toInt(16),
                            state.substring(i+2, i+4).toInt(16),
                            state.substring(i+4, i+6).toInt(16),
                            state.substring(i+6, i+10).toInt()
                        ))
                    }
                }
            }
            if(cb != null) cb()
        }))
    }

    fun setMode(panel: Panel){
        val body = "{ \"directions\": \"%s\", \"mode\": \"%d\" }".format(panel.directions, panel.mode)
        api.setMode(JsonParser().parse(body)).enqueue(ResponseCallback(fun(_: JsonElement){}))
    }

    fun setColor(panel: Panel){
        val body = "{ \"directions\": \"%s\", \"r\": \"%02X\", \"g\": \"%02X\", \"b\": \"%02X\" }"
            .format(panel.directions, panel.r, panel.g, panel.b)
        api.setColor(JsonParser().parse(body)).enqueue(ResponseCallback(fun(_: JsonElement){}))
    }

    fun setGradient(panel: Panel){
        var body = "{ \"directions\": \"%s\", \"length\": \"%d\", \"steps\": [\n".format(panel.directions, panel.gradientSteps.size)
        for(i in 0 until panel.gradientSteps.size){
            val step = panel.gradientSteps[i]
            println(step.r)
            body += "{ \"r\": \"%02X\", \"g\": \"%02X\", \"b\": \"%02X\", \"t\": \"%d\" }".format(step.r, step.g, step.b, step.t)
            if(i < panel.gradientSteps.size-1)
                body += ",\n"
        }
        body += "\n]}"
        println(body)
        api.setGradient(JsonParser().parse(body)).enqueue(ResponseCallback(fun(_: JsonElement){}))
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
