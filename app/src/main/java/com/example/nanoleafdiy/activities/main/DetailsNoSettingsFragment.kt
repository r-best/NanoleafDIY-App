package com.example.nanoleafdiy.activities.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.nanoleafdiy.R

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsNoSettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsNoSettingsFragment: DetailsSettingsFragmentBase {
    override val INDEX: Int = 2

    constructor() : super()
    constructor(directions: String): super(directions)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.create(inflater, R.layout.fragment_details_nosettings, container)
        return binding.root
    }

    override fun onStart() {
        super.start(R.id.nosettings_switchmode_button)
        super.onStart()
    }
}
