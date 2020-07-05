package com.example.nanoleafdiy

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        val networkDiagram: ImageView = findViewById(R.id.network_diagram)
        networkDiagram.setImageDrawable(NetworkDiagram())
        getPanelArrangement()
    }

    class NetworkDiagram : Drawable() {
        override fun draw(canvas: Canvas) {
            val strokePaint: Paint = Paint().apply {
                strokeWidth = 5F
                color = Color.BLUE
                style = Paint.Style.STROKE
            }
            var fillPaint: Paint = Paint().apply {
                strokeWidth = 5F
                color = Color.RED
                style = Paint.Style.FILL
            }
            
            for(panel in panels) {
                val path = panel.getPath()
                canvas.drawPath(path, strokePaint)
                canvas.drawPath(path, fillPaint)
                fillPaint = Paint().apply {
                    strokeWidth = 5F
                    color = Color.GREEN
                    style = Paint.Style.FILL
                }
            }
        }

        override fun setAlpha(p0: Int) {
            TODO("Not yet implemented")
        }

        override fun getOpacity(): Int = PixelFormat.OPAQUE

        override fun setColorFilter(p0: ColorFilter?) {
            TODO("Not yet implemented")
        }
    }
}
