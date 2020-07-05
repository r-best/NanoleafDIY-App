package com.example.nanoleafdiy

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.appcompat.widget.AppCompatImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        val layout: ConstraintLayout = findViewById(R.id.page_container)
        layout.removeAllViews()
        layout.addView(NetworkDiagramView(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        })
    }

    class NetworkDiagramView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
    ): AppCompatImageView(context, attrs, defStyle) {
        private val networkDiagram: NetworkDiagramDrawable = NetworkDiagramDrawable()

        override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
            super.onLayout(changed, left, top, right, bottom)
            setImageDrawable(networkDiagram)
            computeNetworkTopology()
            adjustPosition(width, height)
        }
    }

    class NetworkDiagramDrawable : Drawable() {
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
