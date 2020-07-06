package com.example.nanoleafdiy

import android.content.Context
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment


class MainActivity : AppCompatActivity() {
    var activeDetailsFragment: DetailsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        val layout: ConstraintLayout = findViewById(R.id.network_diagram_container)
        layout.addView(NetworkDiagramView(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        })
    }

    fun openDetailsFragment(panel: Panel){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if(activeDetailsFragment != null)
            fragmentTransaction.remove(activeDetailsFragment!!)
        activeDetailsFragment = DetailsFragment(panel)
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
