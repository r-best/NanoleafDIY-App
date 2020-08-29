package com.example.nanoleafdiy.activities.main.modefragment

import android.graphics.Color
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.activities.main.MainActivity
import com.example.nanoleafdiy.utils.ApiService
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [SolidFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SolidFragment : ModeFragmentBase {
    override val LAYOUT: Int = R.layout.fragment_details_solid
    override val INDEX: Int = 0

    constructor() : super()
    constructor(directions: String): super(directions)

    private lateinit var colorView: TextView

    override fun onStart() {
        super.start(R.id.solid_switchmode_button)
        colorView = (context as MainActivity).findViewById(R.id.color_field)
        colorView.setOnClickListener(::launchColorPicker)
        updateColorDisplay()
        super.onStart()
    }

    override fun onPanelStateFetched() {
        updateColorDisplay()
        (context as MainActivity).redrawDiagram()
    }

    private fun updateColorDisplay(){
        colorView.setBackgroundColor(Color.argb(255, panel.r, panel.g, panel.b))
        colorView.text = String.format("#%02x%02x%02x", panel.r, panel.g, panel.b).toUpperCase(Locale.ROOT)
    }

    private fun launchColorPicker(v: View?){
        val colorPicker = ColorPicker((context as MainActivity), panel.r, panel.g, panel.b)
        colorPicker.show()
        colorPicker.findViewById<Button>(R.id.okColorButton).setOnClickListener(fun(_: View) {
            panel.r = colorPicker.red
            panel.g = colorPicker.green
            panel.b = colorPicker.blue

            ApiService.setColor(panel)
            updateColorDisplay()
            (context as MainActivity).redrawDiagram()

            colorPicker.dismiss()
        })
    }
}
