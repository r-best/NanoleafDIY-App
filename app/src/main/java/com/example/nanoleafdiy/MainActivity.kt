package com.example.nanoleafdiy

import android.content.Context
import android.graphics.*
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

        override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
            super.onLayout(changed, left, top, right, bottom)
            computeNetworkTopology()
            adjustPosition(width, height)
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
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
    }
}
