package com.example.nanoleafdiy.activities.main

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.databinding.FragmentDetailsSolidBinding
import com.example.nanoleafdiy.utils.ApiService
import com.pes.androidmaterialcolorpickerdialog.ColorPicker

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsSolidFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsSolidFragment(directions: String) : DetailsSettingsFragmentBase(directions) {
    override val INDEX: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.create(inflater, R.layout.fragment_details_solid, container)
        (binding as FragmentDetailsSolidBinding).panel = panel
        return binding.root
    }

    override fun onStart() {
        super.start(R.id.solid_switchmode_button)
        (context as MainActivity).findViewById<ConstraintLayout>(R.id.color_field).setOnClickListener(::launchColorPicker)
        super.onStart()
    }

    private fun launchColorPicker(v: View?){
        val colorPicker = ColorPicker((context as MainActivity), panel.r, panel.g, panel.b)
        colorPicker.show()
        colorPicker.findViewById<Button>(R.id.okColorButton).setOnClickListener(fun(_: View) {
            panel.r = colorPicker.red
            panel.g = colorPicker.green
            panel.b = colorPicker.blue

            ApiService.setColor(panel)
            (context as MainActivity).findViewById<TextView>(R.id.color_text).compoundDrawables[0].setTint(
                Color.rgb(panel.r, panel.g, panel.b))
            (context as MainActivity).redrawDiagram()
            binding.invalidateAll()

            colorPicker.dismiss()
        })
    }
}
