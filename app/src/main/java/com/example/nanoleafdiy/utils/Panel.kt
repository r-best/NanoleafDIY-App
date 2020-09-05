package com.example.nanoleafdiy.utils

import android.graphics.Path
import com.example.nanoleafdiy.activities.main.networkdiagram.PANEL_SCALE
import com.example.nanoleafdiy.activities.main.networkdiagram.Vertex
import com.example.nanoleafdiy.activities.main.networkdiagram.cosD
import com.example.nanoleafdiy.activities.main.networkdiagram.sinD

/**
 * Holds all the information needed to draw a Panel on the network diagram, including
 * its coordinates, rotation angle, and references to its neighbors
 */
class Panel {
    // Navigation directions that the controller uses to send instructions to a particular panel
    var directions: String = ""
    var parent: Panel? = null
    var left: Panel? = null
    var right: Panel? = null

    var mode: Int = -1 // Active lighting mode, 0 is solid color, 1 is gradient, 2 rainbow, etc..

    var statesInitialized = mutableListOf(false, false, false, false, false, false)

    // Data for mode 0 (Solid color)
    var r: Int = 255
    var g: Int = 255
    var b: Int = 255

    // Data for mode 1 (custom gradient)
    var gradientSteps = mutableListOf<GradientStep>()

    // Data for mode 2 (blink)
    var blinkSteps = mutableListOf<GradientStep>()

    /*-----------------------------------------------------
     ---------Utilities for drawing network diagram--------
     ------------------------------------------------------*/
    // The coordinates of the bottom-left vertex of the triangle
    var position: Vertex = Pair(0f, 0f)
    // The angle, measured clockwise from East, that points toward vertex 2
    var angle: Float = 0f

    /** Computes the position of the bottom-right vertex */
    fun getV2(): Vertex = Pair(
        position.first + PANEL_SCALE * cosD(
            angle
        ),
        position.second + PANEL_SCALE * sinD(
            angle
        )
    )

    /** Computes the position of the top vertex */
    fun getV3(): Vertex = Pair(
        position.first + PANEL_SCALE * cosD(
            -60f + angle
        ),
        position.second + PANEL_SCALE * sinD(
            -60f + angle
        )
    )

    /** Tests if the given xy point is contained in the triangle */
    fun contains(x: Float, y: Float): Boolean {
        val collisionPath = Path()
        collisionPath.moveTo(x, y)
        collisionPath.lineTo(x+1, y)
        collisionPath.lineTo(x+1, y+1)
        collisionPath.lineTo(x, y)

        collisionPath.op(getPath(), Path.Op.INTERSECT)
        return !collisionPath.isEmpty
    }

    /**
     * Returns a Path object using the triangle's
     * vertices that can be drawn to the screen
     */
    fun getPath(): Path {
        val v2: Vertex = getV2()
        val v3: Vertex = getV3()
        val path = Path()
        path.moveTo(position.first, position.second)
        path.lineTo(v2.first, v2.second)
        path.lineTo(v3.first, v3.second)
        path.lineTo(position.first, position.second)
        return path
    }
}

