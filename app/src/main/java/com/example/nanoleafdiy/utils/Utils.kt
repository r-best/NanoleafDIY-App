package com.example.nanoleafdiy.utils

import com.example.nanoleafdiy.views.panels

data class Quadruple<A, B, C, D>(var first: A, var second: B, var third: C, var fourth: D)

fun getPanel(directions: String): Panel? {
    for(panel in panels){
        if(panel.directions == directions)
            return panel
    }
    return null
}
