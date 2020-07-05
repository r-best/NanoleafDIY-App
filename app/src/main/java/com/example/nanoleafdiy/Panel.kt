package com.example.nanoleafdiy

import android.graphics.Color
import android.graphics.Path

/**
 * Holds all the information of a Panel, including its coordinates,
 * rotation angle, and references to its neighboring Panels
 */
class Panel {
    // Navigation directions that the controller uses to send instructions to a particular panel
    var directions: String = ""
    var parent: Panel? = null
    var left: Panel? = null
    var right: Panel? = null

    // The coordinates of the bottom-left vertex of the triangle
    var position: Vertex = Pair(0f, 0f)
    // The angle, measured clockwise from East, that points toward vertex 2
    var angle: Float = 0f

    var color: Int = Color.GREEN

    /** Computes the position of the bottom-right vertex */
    fun getV2(): Vertex = Pair(
        position.first + PANEL_SCALE * cosD(angle),
        position.second + PANEL_SCALE * sinD(angle)
    )

    /** Computes the position of the top vertex */
    fun getV3(): Vertex = Pair(
        position.first + PANEL_SCALE * cosD(-60f + angle),
        position.second + PANEL_SCALE * sinD(-60f + angle)
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
