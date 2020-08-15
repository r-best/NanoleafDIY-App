package com.example.nanoleafdiy.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.nanoleafdiy.*
import com.example.nanoleafdiy.utils.ApiService
import com.example.nanoleafdiy.utils.Panel
import com.example.nanoleafdiy.views.NetworkDiagramView


class MainActivity : AppCompatActivity() {
    lateinit var networkDiagramView: NetworkDiagramView
    private var activeDetailsFragment: DetailsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        ApiService.init(this)

        networkDiagramView = NetworkDiagramView(
            this
        ).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        }

        findViewById<ConstraintLayout>(R.id.network_diagram_container).addView(networkDiagramView)
    }

    fun redrawDiagram(){
        networkDiagramView.invalidate()
    }

    fun openDetailsFragment(panel: Panel){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if(activeDetailsFragment != null)
            fragmentTransaction.remove(activeDetailsFragment!!)
        activeDetailsFragment =
            DetailsFragment(panel)
        fragmentTransaction.add(R.id.details_container, activeDetailsFragment!!)
        fragmentTransaction.commit()
    }

    fun closeDetailsFragment(){
        if(activeDetailsFragment != null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.remove(activeDetailsFragment!!)
            fragmentTransaction.commit()
        }
    }
}
