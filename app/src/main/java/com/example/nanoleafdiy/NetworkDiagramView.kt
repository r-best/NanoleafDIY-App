package com.example.nanoleafdiy

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentManager

class NetworkDiagramView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): AppCompatImageView(context, attrs, defStyle) {
    private var selectedPanel: Panel? = null

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
        selectedPanel = null
        for(panel in panels) {
            if (panel.contains(event.x, event.y)) {
                panel.selected = true
                selectedPanel = panel
            }
            else {
                panel.selected = false
            }
        }
        if(selectedPanel != null)
            (context as MainActivity).openDetailsFragment(selectedPanel!!)
        else
            (context as MainActivity).closeDetailsFragment()
        invalidate()
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        fillPaint.alpha = if(selectedPanel == null) 255 else 100

        for(panel in panels) {
            if(panel.selected) fillPaint.alpha = 255

            val path = panel.getPath()
            canvas.drawPath(path, strokePaint)
            canvas.drawPath(path, fillPaint)

            if(panel.selected) fillPaint.alpha = 100
        }
    }
}
