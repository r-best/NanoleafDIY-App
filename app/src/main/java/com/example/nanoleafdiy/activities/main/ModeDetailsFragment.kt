package com.example.nanoleafdiy.activities.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nanoleafdiy.utils.Panel
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.activities.main.modefragment.ChooseModeFragment
import com.example.nanoleafdiy.activities.main.modefragment.DetailsGradientFragment
import com.example.nanoleafdiy.activities.main.modefragment.NoSettingsFragment
import com.example.nanoleafdiy.activities.main.modefragment.SolidFragment
import com.example.nanoleafdiy.utils.ApiService
import com.example.nanoleafdiy.utils.getPanel

/**
 * This fragment sits on the main screen below the network diagram
 * It is created when a user clicks on one of the panels in the network diagram, allowing
 * the user to change the panel's settings
 *
 * Contains a child fragment depending on the current panel mode with mode-specific settings, i.e.:
 *  - SolidFragment allows the user to choose a single color for the panel
 *  - GradientFragment allows the user to choose a range of colors for the panel
 *      to fade between, along with fade transition times
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

        setPanelMode(panel.mode)

        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    /**
     * Called by the DetailsChooseModeFragment when the user selects a new mode, makes
     * the API call to change the panel to that mode and switches the active fragment
     * to reflect the change
     *
     * Also called by the fragments with a mode of -1 when the user presses the change
     * mode button, this is just an indicator to swap to the change mode fragment and
     * does not actually change the panel's mode
     */
    fun setPanelMode(mode: Int){
        println("SET PANEL MODE %d".format(mode))
        if(mode > -1 && mode != panel.mode){
            panel.mode = mode
            ApiService.setMode(panel)
            (context as MainActivity).redrawDiagram()
        }
        when(mode){
            -1   -> swapFragment(ChooseModeFragment())
            0    -> swapFragment(SolidFragment(panel.directions))
            1    -> swapFragment(DetailsGradientFragment(panel.directions))
            else -> swapFragment(NoSettingsFragment(panel.directions))
        }
    }

    private fun swapFragment(fragment: Fragment){
        childFragmentManager.beginTransaction()
            .replace(R.id.details_container_inner, fragment)
            .commit()
    }
}
