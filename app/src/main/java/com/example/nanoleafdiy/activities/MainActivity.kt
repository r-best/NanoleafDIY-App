package com.example.nanoleafdiy.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nanoleafdiy.*
import com.example.nanoleafdiy.utils.ApiService
import com.example.nanoleafdiy.utils.Panel
import com.example.nanoleafdiy.views.NetworkDiagramView


class MainActivity : AppCompatActivity() {
    private var activeDetailsFragment: DetailsFragment? = null

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
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if(activeDetailsFragment != null)
            fragmentTransaction.remove(activeDetailsFragment!!)
        activeDetailsFragment = DetailsFragment(panel.directions)
        fragmentTransaction.add(R.id.details_container_outer, activeDetailsFragment!!)
        fragmentTransaction.commit()
    }

    fun closeDetailsFragment(){
        if(activeDetailsFragment != null)
            supportFragmentManager.beginTransaction()
                .remove(activeDetailsFragment!!)
                .commit()
    }
}
