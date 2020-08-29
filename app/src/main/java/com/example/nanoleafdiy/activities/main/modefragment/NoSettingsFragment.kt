package com.example.nanoleafdiy.activities.main.modefragment

import androidx.fragment.app.Fragment
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.activities.main.modefragment.ModeFragmentBase

/**
 * A simple [Fragment] subclass.
 * Use the [NoSettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoSettingsFragment : ModeFragmentBase {
    override val LAYOUT: Int = R.layout.fragment_details_nosettings
    override val INDEX: Int = 2

    constructor() : super()
    constructor(directions: String): super(directions)

    override fun onStart() {
        super.onStart()
    }

    override fun onPanelStateFetched() {}
}
