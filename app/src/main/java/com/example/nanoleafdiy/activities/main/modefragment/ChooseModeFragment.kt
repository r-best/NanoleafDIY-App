package com.example.nanoleafdiy.activities.main.modefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.activities.main.ModeDetailsFragment
import com.example.nanoleafdiy.activities.main.MainActivity
import com.example.nanoleafdiy.utils.SquareImageView

/**
 * A simple [Fragment] subclass.
 * Use the [ChooseModeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChooseModeFragment: Fragment() {

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
                (parentFragment as ModeDetailsFragment).setPanelMode(mode)
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
