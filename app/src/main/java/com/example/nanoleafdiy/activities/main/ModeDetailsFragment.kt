package com.example.nanoleafdiy.activities.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.utils.*

/**
 * This fragment sits on the main screen below the network diagram
 * It is created when a user clicks on one of the panels in the network diagram, allowing
 * the user to change the panel's settings
 *
 * Contains a child fragment depending on the current panel mode with mode-specific settings, i.e.:
 *  - SolidFragment allows the user to choose a single color for the panel (mode 0)
 *  - GradientFragment allows the user to choose a range of colors for the panel
 *      to fade between, along with fade transition times (mode 1)
 *  - NoSettingsFragment is a placeholder for modes with no user-defined settings, like theaterchase and rainbow
 *      - Future state: this will include some global settings, like brightness
 *  - ChooseModeFragment is not tied to a real panel mode (uses -1) and lets the user
 *      select a different mode
 */
class ModeDetailsFragment : Fragment { constructor() : super()
    constructor(directions: String) : super() {
        arguments = Bundle().apply { putString("directions", directions) }
    }
    private lateinit var panel: Panel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        panel = getPanel(arguments?.getString("directions")!!)!!

        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onStart() {
        super.onStart()

        (context as MainActivity).findViewById<TextView>(R.id.paneldetails_palette_name_display).text = matchPalette(panel.palette)
        (context as MainActivity).findViewById<Switch>(R.id.paneldetails_randomize_switch).isChecked = panel.randomize
        (context as MainActivity).findViewById<Switch>(R.id.paneldetails_synchronize_switch).isChecked = panel.synchronize

        val spinner = (context as MainActivity).findViewById<Spinner>(R.id.paneldetails_mode_dropdown)
        ArrayAdapter(context as MainActivity, android.R.layout.simple_spinner_item, PANEL_MODES.map { it.name + " Mode" }).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = it
            spinner.setSelection(panel.mode)
            spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(adapter: AdapterView<*>?) {}
                override fun onItemSelected(adapter: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                    setPanelMode(pos)
                }
            }
        }
    }

    fun setPanelMode(mode: Int){
        println("SET PANEL MODE %d".format(mode))
        if(mode > -1 && mode != panel.mode){
            panel.mode = mode
            ApiService.setMode(panel)
            (context as MainActivity).redrawDiagram()
        }
    }
}
