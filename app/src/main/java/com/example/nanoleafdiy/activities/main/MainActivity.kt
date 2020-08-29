package com.example.nanoleafdiy.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nanoleafdiy.*
import com.example.nanoleafdiy.activities.main.networkdiagram.NetworkDiagramView
import com.example.nanoleafdiy.utils.ApiService
import com.example.nanoleafdiy.utils.Panel


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        ApiService.init(this)
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
