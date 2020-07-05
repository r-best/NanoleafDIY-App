package com.example.nanoleafdiy

import android.graphics.Path
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.properties.Delegates


const val PANEL_SCALE: Int = 300

fun sinD(degrees: Float): Float = sin(degrees * PI.toFloat() / 180f)
fun cosD(degrees: Float): Float = cos(degrees * PI.toFloat() / 180f)

class Panel {
    lateinit var directions: String
    var parent: Panel? = null
    var left: Panel? = null
    var right: Panel? = null

    lateinit var parentVertex: Pair<Float, Float>
    var parentAngle by Delegates.notNull<Float>()

    fun getPath(): Path {
        val v2Offset: Pair<Float, Float> = Pair(
            parentVertex.first + PANEL_SCALE * cosD(parentAngle),
            parentVertex.second + PANEL_SCALE * sinD(parentAngle)
        )
        val v3Offset: Pair<Float, Float> = Pair(
            parentVertex.first + PANEL_SCALE * cosD(-60f + parentAngle),
            parentVertex.second + PANEL_SCALE * sinD(-60f + parentAngle)
        )
        val path = Path()
        path.moveTo(parentVertex.first, parentVertex.second)
        path.lineTo(v2Offset.first, v2Offset.second)
        path.lineTo(v3Offset.first, v3Offset.second)
        path.lineTo(parentVertex.first, parentVertex.second)
        return path
    }
}

var panels: MutableList<Panel> = mutableListOf()

/**
 * Note: algorithm fails on empty left nodes, i.e. "...(X(..."
 */
fun getPanelArrangement(){
    // Make request to panel controller, stub for now
    val tree: String = "(((XX)X)((XX)((XX)X)))"
//    val tree: String = "(XX)"
//    val tree: String = "((XX)(XX))"
//    val tree: String = "((XX)X)"

    // Create root panel
    panels.add(Panel().apply {
        directions = ""
        parentVertex = Pair(500f, 500f)
        parentAngle = 0f
    })

    var active: Panel = panels[0]
    var nextIsRight: Boolean = false
    for(i in 1 until tree.length-1){
        if(tree[i] == '('){
            val child: Panel = Panel().apply { parent = active }
            if(nextIsRight){
                child.directions = active.directions+"R"
                child.parentVertex = Pair(
                    active.parentVertex.first + PANEL_SCALE * cosD(-60f + active.parentAngle),
                    active.parentVertex.second + PANEL_SCALE * sinD(-60f + active.parentAngle)
                )
                child.parentAngle = active.parentAngle + 60
                active.right = child
                nextIsRight = false
            }
            else{
                child.directions = active.directions+"L"
                child.parentVertex = active.parentVertex
                child.parentAngle = active.parentAngle - 60
                active.left = child
            }
            panels.add(child)
            active = child
        }
        else if(tree[i] == ')'){
            active = active.parent!!
            nextIsRight = true
        }
    }
    println(panels.size)
}
