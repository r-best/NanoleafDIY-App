package com.example.nanoleafdiy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

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
        ApiService.getNetworkTopology(fun(result: String){
            computeNetworkTopology(result)
            adjustPosition(width, height)
            invalidate()
        })
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

        for(panel in panels) {
            if(panel.selected){
                strokePaint.strokeWidth = 10f
                strokePaint.color = Color.GRAY
            }

            fillPaint.color = Color.rgb(panel.r, panel.g, panel.b)

            val path = panel.getPath()
            canvas.drawPath(path, fillPaint)
            canvas.drawPath(path, strokePaint)

            if(panel.selected){
                strokePaint.strokeWidth = 5f
                strokePaint.color = Color.BLACK
            }
        }
    }
}
