package com.example.nanoleafdiy

import android.graphics.Path
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.min
import kotlin.properties.Delegates
import java.util.Collections.max as maxOf
import java.util.Collections.min as minOf


typealias Vertex = Pair<Float, Float>

val PADDING = 20
var PANEL_SCALE: Int = 10

// I was never any good with radians, I want to use degrees instead
val sinD = { degrees: Float -> sin(degrees * PI.toFloat() / 180f) }
val cosD = { degrees: Float -> cos(degrees * PI.toFloat() / 180f) }

class Panel {
    // Navigation directions that the controller uses to send instructions to a particular panel
    lateinit var directions: String
    var parent: Panel? = null
    var left: Panel? = null
    var right: Panel? = null

    // The coordinates of the bottom-left vertex of the triangle
    lateinit var position: Vertex
    // The angle, measured clockwise from East, that points toward vertex 2
    var angle: Float = 0f

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

// Don't technically need this since the network is a doubly linked list,
// but it makes iterating all the panels easier and I'm relishing in writing
// code for a device with more than 2KB of memory
var panels: MutableList<Panel> = mutableListOf()

/**
 * Reads the string encoding of the panel network structure
 * and builds a linked list representation from it
 *
 * Note: algorithm fails on empty left nodes, i.e. "...(X(..."
 */
fun computeNetworkTopology(){
    // Make request to panel controller, stub for now
    val tree: String = "(((XX)X)((XX)((XX)X)))"
//    val tree: String = "(XX)"
//    val tree: String = "((XX)(XX))"
//    val tree: String = "((XX)X)"

    // Create root panel
    panels = mutableListOf()
    panels.add(Panel().apply {
        directions = ""
        position = Pair(0f, 0f)
        angle = 0f
    })

    // Iterate through the string representation and construct the tree from it
    var active: Panel = panels[0]
    var nextIsRight: Boolean = false
    for(i in 1 until tree.length-1){
        // On an open parenthesis, we add a new panel to the left or right,
        // depending on the value of `nextIsRight`
        if(tree[i] == '('){
            val child: Panel = Panel().apply { parent = active }
            // If we're adding a panel to the right, its position vertex is
            // this panel's top vertex, and it's rotated 60 degrees clockwise
            if(nextIsRight){
                child.directions = active.directions+"R"
                child.position = active.getV3()
                child.angle = active.angle + 60
                active.right = child
                nextIsRight = false
            }
            // If it's a left, it shares a position vertex with this panel
            // and is simply rotated 60 degrees counterclockwise
            else{
                child.directions = active.directions+"L"
                child.position = active.position
                child.angle = active.angle - 60
                active.left = child
            }
            panels.add(child)
            active = child
        }
        // On a close parenthesis, we retreat to the parent panel and
        // mark that the next panel will be a right
        else if(tree[i] == ')'){
            active = active.parent!!
            nextIsRight = true
        }
    }
}

/**
 * Used to fit network diagram on the screen nicely
 * Finds the topmost and leftmost vertices and repositions/resizes
 * the diagram so that they're a fixed distance from their respective
 * side of the screen
 */
fun adjustPosition(boundsX: Int, boundsY: Int){
    // Get size of network diagram
    var (minX, maxX, minY, maxY) = _getDiagramBounds()

    // Compute what scaling factor we need to make the diagram fill the screen,
    // with a small bit of padding on the edges
    val scaleFactor = min((boundsX - PADDING*2) / (maxX - minX), (boundsY - PADDING*2) / (maxY - minY))
    println("Scale Factor: $scaleFactor")
    if(scaleFactor != 1f){
        // Adjust the scale of the diagram
        PANEL_SCALE = (PANEL_SCALE * scaleFactor).toInt()

        // Recompute vertices & bounds with new panel size
        for(panel in panels){
            if(panel.parent != null){
                if(panel.directions.endsWith("R"))
                    panel.position = panel.parent!!.getV3()
                else
                    panel.position = panel.parent!!.position
            }
        }
        val (temp1, temp2, temp3, temp4) = _getDiagramBounds()
        minX = temp1; maxX = temp2; minY = temp3; maxY = temp4
    }

    // Reposition the diagram now that it's appropriately scaled
    for(panel in panels){
        panel.position = Pair(
            panel.position.first - minX + PADDING,
            panel.position.second - minY + PADDING
        )
    }
}

/**
 * Computes the leftmost, rightmost, topmost, and bottommost
 * bounds of the network diagram, returning them in that order
 */
fun _getDiagramBounds(): Quadruple<Float, Float, Float, Float>{
    var minX: Float = panels[0].position.first
    var maxX: Float = panels[0].position.first
    var minY: Float = panels[0].position.second
    var maxY: Float = panels[0].position.second
    for(panel in panels){
        val v2 = panel.getV2()
        val v3 = panel.getV3()

        minX = minOf(listOf(minX, panel.position.first, v2.first, v3.first))
        maxX = maxOf(listOf(minX, panel.position.first, v2.first, v3.first))
        minY = minOf(listOf(minY, panel.position.second, v2.second, v3.second))
        maxY = maxOf(listOf(minY, panel.position.second, v2.second, v3.second))
    }
    return Quadruple(minX, maxX, minY, maxY)
}
