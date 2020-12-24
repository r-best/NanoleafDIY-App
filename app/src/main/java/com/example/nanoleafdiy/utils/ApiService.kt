package com.example.nanoleafdiy.utils

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
import java.net.InetAddress


class ApiService(val name: String, host: InetAddress, port: Int) {
    companion object {
        interface API {
            @GET("/") fun health(): Call<Void>

            /** Obtain the cached panel network configuration on the controller */
            @GET("network") fun getNetworkTopology(): Call<JsonElement>

            /** Obtain the current panel network configuration, forcing the controller to recompute the network topology */
            @GET("network/refresh") fun getNetworkTopologyWithRefresh(): Call<JsonElement>

            /** Retrieve the current state of a panel, including its active mode and any settings related to that mode */
            @POST("panels/state") fun getPanelState(@Body body: JsonElement): Call<JsonElement>

            /** Sets the active lighting mode of a given panel (solid color, gradient, rainbow, etc..) */
            @POST("panels/mode") fun setMode(@Body body: JsonElement): Call<JsonElement>

            /** Sets the brightness of a given panel (0-255) */
            @POST("panels/brightness") fun setBrightness(@Body body: JsonElement): Call<JsonElement>

            /** Set the stored gradient state of a given panel */
            @POST("panels/palette") fun setPalette(@Body body: JsonElement): Call<JsonElement>
        }
    }

    private var api: API

    init {
        api = Retrofit.Builder()
            .baseUrl("http:/%s:%d".format(host, port))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)
    }

    fun health(resolve: (Boolean) -> Unit){
        api.health().enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                resolve(true)
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.printStackTrace()
                connectedServices.remove(name)
                resolve(false)
            }
        })
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
            panel.brightness = state.substring(1, 4).toInt()
            panel.randomize = state.substring(4, 5) == "1"
            panel.synchronize = state.substring(5, 6) == "1"

            panel.palette = mutableListOf()
            for(i in 7 until state.length-1 step 10){
                panel.palette.add(PaletteColor(
                    state.substring(i, i+2).toInt(16),
                    state.substring(i+2, i+4).toInt(16),
                    state.substring(i+4, i+6).toInt(16),
                    state.substring(i+6, i+10).toInt()
                ))
            }
            if(cb != null) cb()
        }))
    }

    fun setMode(panel: Panel){
        val body = "{ \"directions\": \"%s\", \"mode\": \"%d\" }".format(panel.directions, panel.mode)
        api.setMode(JsonParser().parse(body)).enqueue(ResponseCallback(fun(_: JsonElement){}))
    }

    fun setBrightness(panel: Panel){
        val body = "{ \"directions\": \"%s\", \"brightness\": \"%d\" }".format(panel.directions, panel.brightness)
        api.setBrightness(JsonParser().parse(body)).enqueue(ResponseCallback(fun(_: JsonElement){}))
    }

    fun setPalette(panel: Panel){
        var body = "{ \"directions\": \"%s\", \"length\": \"%d\", \"randomize\": \"%s\", \"synchronize\": \"%s\", \"steps\": [\n"
            .format(panel.directions, panel.palette.size, panel.randomize, panel.synchronize)
        for(i in 0 until panel.palette.size){
            val step = panel.palette[i]
            body += "{ \"r\": \"%02X\", \"g\": \"%02X\", \"b\": \"%02X\", \"t\": \"%d\" }".format(step.r, step.g, step.b, step.t)
            if(i < panel.palette.size-1)
                body += ",\n"
        }
        body += "\n]}"
        println(body)
        api.setPalette(JsonParser().parse(body)).enqueue(ResponseCallback(fun(_: JsonElement){}))
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
            ToastManager.makeText(t.message!!, Toast.LENGTH_LONG)
        }
    }
}
