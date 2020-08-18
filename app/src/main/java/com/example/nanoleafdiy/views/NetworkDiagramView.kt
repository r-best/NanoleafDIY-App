package com.example.nanoleafdiy.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import com.example.nanoleafdiy.utils.Panel
import com.example.nanoleafdiy.activities.main.MainActivity
import com.example.nanoleafdiy.utils.ApiService

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
        ApiService.getNetworkTopology(true, fun(result: String){
            parseNetworkTopology(result)
            for(panel in panels)
                ApiService.getPanelState(panel){ invalidate() }
            adjustPosition(width, height)
            invalidate()
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        for(panel in panels) {
            if (panel.contains(event.x, event.y)) {
                if(panel != selectedPanel){
                    selectedPanel = panel
                    (context as MainActivity).openDetailsFragment(selectedPanel!!)
                    invalidate()
                }
                return super.onTouchEvent(event)
            }
        }
        (context as MainActivity).closeDetailsFragment()
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw controller connected to first panel
        if(panels.size > 0) {
            val controllerPath = Path()
            controllerPath.moveTo(
                panels[0].position.first + (PANEL_SCALE / 2.5f * cosD(panels[0].angle)),
                panels[0].position.second + (PANEL_SCALE / 2.5f * sinD(panels[0].angle))
            )
            controllerPath.rLineTo(PANEL_SCALE / 15 * sinD(-panels[0].angle), PANEL_SCALE / 15 * cosD(-panels[0].angle))
            controllerPath.rLineTo(PANEL_SCALE / 5 * cosD(panels[0].angle), PANEL_SCALE / 5 * sinD(panels[0].angle))
            controllerPath.rLineTo(-PANEL_SCALE / 15 * sinD(-panels[0].angle), -PANEL_SCALE / 15 * cosD(-panels[0].angle))
            fillPaint.color = Color.BLACK
            canvas.drawPath(controllerPath, fillPaint)
        }

        // Draw all panels
        for(panel in panels) {
            // If this panel is selected, highlight it with a different stroke color
            if(panel == selectedPanel){
                strokePaint.strokeWidth = 10f
                strokePaint.color = Color.GRAY
            }

            if(panel.mode == 0)
                fillPaint.color = Color.rgb(panel.r, panel.g, panel.b)
            else
                fillPaint.color = Color.rgb(255, 255, 255)

            val path = panel.getPath()
            canvas.drawPath(path, fillPaint)
            canvas.drawPath(path, strokePaint)

            // Change the stroke color back
            if(panel == selectedPanel){
                strokePaint.strokeWidth = 5f
                strokePaint.color = Color.BLACK
            }
        }
    }
}
