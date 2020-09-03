package com.example.nanoleafdiy.utils

import com.example.nanoleafdiy.activities.main.modefragment.GradientFragment
import com.example.nanoleafdiy.activities.main.modefragment.ModeFragmentBase
import com.example.nanoleafdiy.activities.main.modefragment.NoSettingsFragment
import com.example.nanoleafdiy.activities.main.modefragment.SolidFragment
import com.example.nanoleafdiy.activities.main.networkdiagram.panels
import kotlin.reflect.KClass


data class GradientStep(var r: Int, var g: Int, var b: Int, var t: Int)
data class Quadruple<A, B, C, D>(var first: A, var second: B, var third: C, var fourth: D)

fun getPanel(directions: String): Panel? {
    for(panel in panels){
        if(panel.directions == directions)
            return panel
    }
    return null
}

data class Mode(val name: String, val settingsFragment: KClass<out ModeFragmentBase>)
val PANEL_MODES: List<Mode> = listOf(
    Mode("Solid Color", SolidFragment::class),
    Mode("Custom Gradient", GradientFragment::class),
    Mode("Rainbow", NoSettingsFragment::class),
    Mode("Theater Chase", NoSettingsFragment::class),
    Mode("Theater Chase Rainbow", NoSettingsFragment::class)
)

abstract class Preset(var name: String)

class SolidPreset(name: String, var r: Int, var g: Int, var b: Int): Preset(name)
val SOLID_PRESETS: List<SolidPreset> = listOf(
    SolidPreset("Pinkish", 150, 20, 45),
    SolidPreset("Candle", 255, 147, 41),
    SolidPreset("Clear Blue Sky", 64, 156, 255)
)

class GradientPreset(name: String, var steps: MutableList<GradientStep>): Preset(name)
val GRADIENT_PRESETS: List<GradientPreset> = listOf(
    GradientPreset("1", mutableListOf(
        GradientStep(255, 102, 68, 500),
        GradientStep(0, 0, 0, 500),
        GradientStep(0, 255, 0, 500),
        GradientStep(255, 0, 0, 500),
        GradientStep(0, 255, 255, 500),
        GradientStep(255, 255, 0, 500),
        GradientStep(0, 1, 0, 500)
    )),
    GradientPreset("Siren", mutableListOf(
        GradientStep(255, 0, 0, 500),
        GradientStep(0, 0, 255, 500)
    ))
)
