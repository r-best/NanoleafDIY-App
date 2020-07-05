package com.example.nanoleafdiy

import android.content.Context
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
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
            for(panel in panels)
                if(panel.contains(event.x, event.y))
                    panel.color = Color.BLUE
            invalidate()
            return super.onTouchEvent(event)
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            for(panel in panels) {
                val path = panel.getPath()
                canvas.drawPath(path, strokePaint)
                fillPaint.color = panel.color
                canvas.drawPath(path, fillPaint)
            }
        }
    }
}
