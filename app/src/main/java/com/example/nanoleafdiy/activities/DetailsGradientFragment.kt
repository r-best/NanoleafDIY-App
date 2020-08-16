package com.example.nanoleafdiy.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.databinding.FragmentDetailsGradientBinding
import com.example.nanoleafdiy.utils.Panel
import com.example.nanoleafdiy.utils.getPanel

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsGradientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsGradientFragment : Fragment { constructor() : super()
    constructor(directions: String) : super() {
        arguments = Bundle().apply { putString("directions", directions) }
    }
    private lateinit var binding: FragmentDetailsGradientBinding
    private lateinit var panel: Panel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        panel = getPanel(arguments?.getString("directions")!!)!!

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details_gradient, container, false)
        binding.panel = panel
        return binding.root
    }

    override fun onStart() {
        (context as MainActivity).findViewById<Button>(R.id.gradient_switchmode_button).setOnClickListener{(parentFragment as DetailsFragment).setPanelMode(-1)}
        super.onStart()
    }
}
