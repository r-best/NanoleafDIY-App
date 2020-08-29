package com.example.nanoleafdiy.activities.main.networkdiagram

import com.example.nanoleafdiy.utils.Panel
import com.example.nanoleafdiy.utils.Quadruple
import kotlin.math.*
import java.util.Collections.max as maxOf
import java.util.Collections.min as minOf


typealias Vertex = Pair<Float, Float>

// I was never any good with radians, I want to use degrees instead
val sinD = { degrees: Float -> sin(degrees * PI.toFloat() / 180f) }
val cosD = { degrees: Float -> cos(degrees * PI.toFloat() / 180f) }

// Buffer between the diagram and the edge of the screen
const val PADDING = 20

// Don't technically need this since the network is a doubly linked list,
// but it makes iterating all the panels easier and I'm relishing in writing
// code for a device with more than 2KB of memory
var panels: MutableList<Panel> = mutableListOf()

// Length of one side of a triangle, gets scaled to fit the diagram nicely on the screen
var PANEL_SCALE: Int = 10

/**
 * Reads the string encoding of the panel network structure
 * and builds a linked list representation from it
 *
 * Note: algorithm fails on empty left nodes, i.e. "...(X(..."
 */
fun parseNetworkTopology(tree: String){
//    val tree: String = "(((XX)X)(X((XX)X)))"
//    val tree: String = "()"
//    val tree: String = "(XX)"
//    val tree: String = "((XX)(XX))"
//    val tree: String = "((XX)X)"

    // Initialize list & create root panel
    panels = mutableListOf(Panel())

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
    val scaleFactor = min((boundsX - PADDING *2) / (maxX - minX), (boundsY - PADDING *2) / (maxY - minY))
    if(scaleFactor != 1f){
        // Adjust the scale of the diagram
        PANEL_SCALE = (PANEL_SCALE * scaleFactor).toInt()

        // Recompute vertices & bounds with new panel size
        for(panel in panels){
            // Same rule as during initial tree creation, left panels need to be
            // positioned on their parent's position, and right ones are positioned
            // on their parent's top vertex (v3)
            if(panel.parent != null){
                if(panel.directions.endsWith("R"))
                    panel.position = panel.parent!!.getV3()
                else
                    panel.position = panel.parent!!.position
            }
        }
        val (temp1, _, temp3, _) = _getDiagramBounds()
        minX = temp1; minY = temp3
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
fun _getDiagramBounds(): Quadruple<Float, Float, Float, Float> {
    var minX: Float = panels[0].position.first
    var maxX: Float = panels[0].position.first
    var minY: Float = panels[0].position.second
    var maxY: Float = panels[0].position.second
    // Go through each panel in the list and check if any of its vertices are a new min or max
    for(panel in panels){
        val v2 = panel.getV2()
        val v3 = panel.getV3()

        minX = minOf(listOf(minX, panel.position.first, v2.first, v3.first))
        maxX = maxOf(listOf(maxX, panel.position.first, v2.first, v3.first))
        minY = minOf(listOf(minY, panel.position.second, v2.second, v3.second))
        maxY = maxOf(listOf(maxY, panel.position.second, v2.second, v3.second))
    }

    // This is a mess of trigonometry, but just know that it's accounting for the little controller
    // box drawn on the edge of the first panel, we don't want that to get drawn out of frame
    val controllerV1 = Vertex(
        panels[0].position.first + (PANEL_SCALE / 2.5f * cosD(panels[0].angle)) + (PANEL_SCALE / 15f * sinD(-panels[0].angle)),
        panels[0].position.second + (PANEL_SCALE / 2.5f * sinD(panels[0].angle)) + (PANEL_SCALE / 15f * cosD(-panels[0].angle))
    )
    val controllerV2 = Vertex(
        controllerV1.first + (PANEL_SCALE / 5 * cosD(panels[0].angle)),
        controllerV1.second + (PANEL_SCALE / 5 * sinD(panels[0].angle))
    )
    minX = minOf(listOf(minX, controllerV1.first, controllerV2.first))
    maxX = maxOf(listOf(maxX, controllerV1.first, controllerV2.first))
    minY = minOf(listOf(minY, controllerV1.second, controllerV2.second))
    maxY = maxOf(listOf(maxY, controllerV1.second, controllerV2.second))

    return Quadruple(minX, maxX, minY, maxY)
}
