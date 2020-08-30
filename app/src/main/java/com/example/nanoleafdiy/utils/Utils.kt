package com.example.nanoleafdiy.utils

import com.example.nanoleafdiy.activities.main.networkdiagram.panels


data class GradientStep(var r: Int, var g: Int, var b: Int, var t: Int)
data class Quadruple<A, B, C, D>(var first: A, var second: B, var third: C, var fourth: D)

fun getPanel(directions: String): Panel? {
    for(panel in panels){
        if(panel.directions == directions)
            return panel
    }
    return null
}

abstract class Preset(var name: String)

class SolidPreset(name: String, var r: Int, var g: Int, var b: Int): Preset(name)
val SOLID_PRESETS: List<SolidPreset> = listOf(
    SolidPreset("Pinkish", 150, 20, 45),
    SolidPreset("Candle", 255, 147, 41),
    SolidPreset("Clear Blue Sky", 64, 156, 255)
)

class GradientPreset(name: String, var steps: MutableList<GradientStep>): Preset(name)
val GRADIENT_PRESETS: List<GradientPreset> = listOf(
)
