package com.example.nanoleafdiy

import android.content.Context
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.appcompat.widget.AppCompatImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        val layout: LinearLayout = findViewById(R.id.network_diagram_container)
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
        private var noneSelected: Boolean = true

        private val strokePaint: Paint = Paint().apply {
            strokeWidth = 5F
            color = Color.BLACK
            style = Paint.Style.STROKE
        }
        private var fillPaint: Paint = Paint().apply {
            strokeWidth = 5F
            color = Color.RED
            style = Paint.Style.FILL
        }

        override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
            super.onLayout(changed, left, top, right, bottom)
            computeNetworkTopology()
            adjustPosition(width, height)
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            noneSelected = true
            for(panel in panels)
                if(panel.contains(event.x, event.y)) {
                    panel.selected = true
                    noneSelected = false
                }
                else panel.selected = false
            invalidate()
            return super.onTouchEvent(event)
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            fillPaint.alpha = if(noneSelected) 255 else 100

            for(panel in panels) {
                if(panel.selected) fillPaint.alpha = 255

                val path = panel.getPath()
                canvas.drawPath(path, strokePaint)
                canvas.drawPath(path, fillPaint)

                if(panel.selected) fillPaint.alpha = 100
            }
        }
    }
}
