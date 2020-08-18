package com.example.nanoleafdiy.activities.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.databinding.FragmentDetailsGradientBinding
import com.example.nanoleafdiy.utils.ApiService

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsGradientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsGradientFragment(directions: String): DetailsSettingsFragmentBase(directions){
    override val INDEX: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.create(inflater, R.layout.fragment_details_gradient, container)
        (binding as FragmentDetailsGradientBinding).panel = panel
        return binding.root
    }

    override fun onStart() {
        super.start(R.id.gradient_switchmode_button)
        super.onStart()
    }
}
