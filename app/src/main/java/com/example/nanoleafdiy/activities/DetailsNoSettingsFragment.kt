package com.example.nanoleafdiy.activities

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
class DetailsNoSettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details_nosettings, container, false)
    }

    override fun onStart() {
        (context as MainActivity).findViewById<Button>(R.id.nosettings_switchmode_button).setOnClickListener{(parentFragment as DetailsFragment).setPanelMode(-1)}
        super.onStart()
    }
}
