package com.example.nanoleafdiy.activities.main.modefragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.activities.main.MainActivity
import com.example.nanoleafdiy.activities.presets.PresetsActivity
import com.example.nanoleafdiy.utils.ApiService
import com.example.nanoleafdiy.utils.SOLID_PRESETS
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
        super.onStart()
        colorView = (context as MainActivity).findViewById(R.id.color_field)
        colorView.setOnClickListener(::launchColorPicker)
        (context as MainActivity).findViewById<Button>(R.id.solidpresets_button).setOnClickListener{
            val intent = Intent(context, PresetsActivity::class.java).apply { putExtra("mode", INDEX) }
            startActivityForResult(intent, 0)
        }
        updateColorDisplay()
    }

    override fun onPanelStateFetched() {
        updateColorDisplay()
        (context as MainActivity).redrawDiagram()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && data != null) {
            val color = SOLID_PRESETS[data.getIntExtra("preset_index", 0)]
            setColor(color.r, color.g, color.b)
        }
    }

    private fun updateColorDisplay(){
        colorView.setBackgroundColor(Color.argb(255, panel.r, panel.g, panel.b))
        colorView.text = String.format("#%02x%02x%02x", panel.r, panel.g, panel.b).toUpperCase(Locale.ROOT)
    }

    private fun launchColorPicker(v: View?){
        val colorPicker = ColorPicker((context as MainActivity), panel.r, panel.g, panel.b)
        colorPicker.show()
        colorPicker.findViewById<Button>(R.id.okColorButton).setOnClickListener(fun(_: View) {
            setColor(colorPicker.red, colorPicker.green, colorPicker.blue)
            colorPicker.dismiss()
        })
    }

    private fun setColor(r: Int, g: Int, b: Int){
        panel.r = r
        panel.g = g
        panel.b = b
        ApiService.setColor(panel)
        updateColorDisplay()
        (context as MainActivity).redrawDiagram()
    }
}
