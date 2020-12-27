package com.example.nanoleafdiy.activities.lightpanels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nanoleafdiy.*
import com.example.nanoleafdiy.activities.lightpanels.networkdiagram.NetworkDiagramView
import com.example.nanoleafdiy.utils.ApiService
import com.example.nanoleafdiy.utils.Panel
import com.example.nanoleafdiy.utils.ToastManager
import com.example.nanoleafdiy.utils.connectedServices


class LightPanelsActivity : AppCompatActivity() {

    lateinit var serviceName: String
    var api: ApiService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lightpanels)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        serviceName = intent.getStringExtra("serviceName")!!

        api = connectedServices[serviceName]
        fun fail(){ ToastManager.makeText("Lost connection to $serviceName", Toast.LENGTH_LONG); finish() }
        if(api == null)
            fail()
        api?.health {
            if(!it)
                fail()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    fun redrawDiagram(){
        findViewById<NetworkDiagramView>(R.id.network_view).invalidate()
    }

    fun openDetailsFragment(panel: Panel){
        supportFragmentManager.beginTransaction()
            .replace(R.id.details_container_outer, ModeDetailsFragment(panel.directions))
            .commit()
    }

    fun closeDetailsFragment(){
        val fragment = supportFragmentManager.findFragmentById(R.id.details_container_outer)
        if(fragment != null) supportFragmentManager.beginTransaction().remove(fragment).commit()
    }
}
