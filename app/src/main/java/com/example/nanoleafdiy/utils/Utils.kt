package com.example.nanoleafdiy.utils

import com.example.nanoleafdiy.activities.lightpanels.networkdiagram.panels


data class PaletteColor(var r: Int, var g: Int, var b: Int, var t: Int)
data class Quadruple<A, B, C, D>(var first: A, var second: B, var third: C, var fourth: D)

val connectedServices: LinkedHashMap<String, ApiService> = LinkedHashMap()

fun getPanel(directions: String): Panel? {
    for(panel in panels){
        if(panel.directions == directions)
            return panel
    }
    return null
}

fun matchPalette(palette: MutableList<PaletteColor>): String {
    for (preset in PALETTE_PRESETS)
        if(preset.colors.size == palette.size)
            if(preset.colors.containsAll(palette) && palette.containsAll(preset.colors))
                return preset.name
    return "Custom"
}

data class Mode(val name: String)
val PANEL_MODES: List<Mode> = listOf(
    Mode("Fade"),
    Mode("Blink"),
    Mode("Rainbow"),
    Mode("Theater Chase"),
    Mode("Theater Chase Rainbow")
)

class Palette(var name: String, var colors: MutableList<PaletteColor>)
val PALETTE_PRESETS: MutableList<Palette> = mutableListOf(
    Palette("1", mutableListOf(
        PaletteColor(255, 102, 68, 500),
        PaletteColor(0, 0, 0, 500),
        PaletteColor(0, 255, 0, 500),
        PaletteColor(255, 0, 0, 500),
        PaletteColor(0, 255, 255, 500),
        PaletteColor(255, 255, 0, 500),
        PaletteColor(0, 1, 0, 500)
    )),
    Palette("Siren", mutableListOf(
        PaletteColor(255, 0, 0, 500),
        PaletteColor(0, 0, 255, 500)
    )),
    Palette("Blinking", mutableListOf(
        PaletteColor(255, 255, 255, 500),
        PaletteColor(0, 0, 0, 500)
    ))
)
