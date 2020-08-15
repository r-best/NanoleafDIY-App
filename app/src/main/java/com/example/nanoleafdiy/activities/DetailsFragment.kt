package com.example.nanoleafdiy.activities

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.nanoleafdiy.utils.ApiService
import com.example.nanoleafdiy.utils.Panel
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.databinding.FragmentDetailsBinding
import com.pes.androidmaterialcolorpickerdialog.ColorPicker

class DetailsFragment constructor(var panel: Panel): Fragment() {
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_details,
            container,
            false
        )
        binding.panel = panel

        return binding.root
    }

    override fun onStart() {
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
            (context as MainActivity).findViewById<TextView>(R.id.color_text).compoundDrawables[0].setTint(Color.rgb(panel.r, panel.g, panel.b))
            (context as MainActivity).redrawDiagram()
            binding.invalidateAll()

            colorPicker.dismiss()
        })
    }
}
