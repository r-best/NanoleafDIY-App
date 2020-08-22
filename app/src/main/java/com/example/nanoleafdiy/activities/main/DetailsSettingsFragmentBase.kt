package com.example.nanoleafdiy.activities.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.databinding.ViewDataBinding
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.utils.ApiService
import com.example.nanoleafdiy.utils.Panel
import com.example.nanoleafdiy.utils.getPanel

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsSettingsFragmentBase.newInstance] factory method to
 * create an instance of this fragment.
 */
abstract class DetailsSettingsFragmentBase : Fragment { constructor() : super()
    constructor(directions: String) : super() {
        arguments = Bundle().apply { putString("directions", directions) }
    }
    protected abstract val LAYOUT: Int // Set in subclasses with the subclass' layout R value
    protected abstract val INDEX: Int // Set in subclasses with subclass' index number (0 for solid, 1 for gradient, etc..)
    protected lateinit var panel: Panel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(LAYOUT, container, false)
    }

    protected fun start(resource: Int) {
        panel = getPanel(arguments?.getString("directions")!!)!!
        (context as MainActivity).findViewById<Button>(resource).setOnClickListener{(parentFragment as DetailsFragment).setPanelMode(-1)}
        if(!panel.statesInitialized[INDEX])
            ApiService.getPanelState(panel){
                if(context != null) // Check for context to be not null bc it's possible for user to back out of fragment before this executes, which causes crash
                    this.onPanelStateFetched()
            }
    }

    /**
     * Callback that is called after the `start` function loads the panel's state
     * Implemented by individual mode fragments with logic for updating their UI with the newly loaded state data
     **/
    protected abstract fun onPanelStateFetched()
}
