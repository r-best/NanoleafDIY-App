package com.example.nanoleafdiy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.nanoleafdiy.databinding.FragmentDetailsBinding
import com.pes.androidmaterialcolorpickerdialog.ColorPicker

class DetailsFragment constructor(var panel: Panel): Fragment() {
    lateinit var binding: FragmentDetailsBinding

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
        (context as MainActivity).findViewById<TextView>(R.id.hex_color).setOnClickListener(::launchColorPicker)

        super.onStart()
    }

    fun launchColorPicker(v: View?){
        val colorPicker = ColorPicker((context as MainActivity), panel.r, panel.g, panel.b)
        colorPicker.show()
        colorPicker.findViewById<Button>(R.id.okColorButton).setOnClickListener(fun(v: View) {
            panel.r = colorPicker.red
            panel.g = colorPicker.green
            panel.b = colorPicker.blue

            (context as MainActivity).redrawDiagram()
            binding.invalidateAll()

            colorPicker.dismiss()
        })
    }
}
