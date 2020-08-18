package com.example.nanoleafdiy.activities.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.utils.SquareImageView
import com.example.nanoleafdiy.utils.getPanel

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsChooseModeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsChooseModeFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details_choosemode, container, false)
    }

    override fun onStart() {
        val registerListener = fun(layoutElement: Int, mode: Int) {
            (context as MainActivity).findViewById<SquareImageView>(layoutElement).setOnClickListener{
                (parentFragment as DetailsFragment).setPanelMode(mode)
            }
        }
        registerListener(R.id.modeSelect_solid, 0)
        registerListener(R.id.modeSelect_gradient, 1)
        registerListener(R.id.modeSelect_rainbow, 2)
        registerListener(R.id.modeSelect_theaterchase, 3)
        registerListener(R.id.modeSelect_theaterchaserainbow, 4)
        super.onStart()
    }
}
