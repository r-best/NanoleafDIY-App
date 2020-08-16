package com.example.nanoleafdiy.activities

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.databinding.FragmentDetailsSolidBinding
import com.example.nanoleafdiy.utils.ApiService
import com.example.nanoleafdiy.utils.Panel
import com.example.nanoleafdiy.utils.getPanel
import com.pes.androidmaterialcolorpickerdialog.ColorPicker

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsSolidFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsSolidFragment : Fragment { constructor() : super()
    constructor(directions: String) : super() {
        arguments = Bundle().apply { putString("directions", directions) }
    }
    private lateinit var binding: FragmentDetailsSolidBinding
    private lateinit var panel: Panel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        panel = getPanel(arguments?.getString("directions")!!)!!

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details_solid, container, false)
        binding.panel = panel
        return binding.root
    }

    override fun onStart() {
        (context as MainActivity).findViewById<Button>(R.id.solid_switchmode_button).setOnClickListener{(parentFragment as DetailsFragment).setPanelMode(-1)}
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
