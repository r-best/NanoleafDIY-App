package com.example.nanoleafdiy.activities.main.modefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.nanoleafdiy.activities.main.ModeDetailsFragment
import com.example.nanoleafdiy.activities.main.MainActivity
import com.example.nanoleafdiy.utils.ApiService
import com.example.nanoleafdiy.utils.Panel
import com.example.nanoleafdiy.utils.getPanel

/**
 * A simple [Fragment] subclass.
 * Use the [ModeFragmentBase.newInstance] factory method to
 * create an instance of this fragment.
 */
abstract class ModeFragmentBase : Fragment {
    constructor() : super()
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
        (context as MainActivity).findViewById<Button>(resource).setOnClickListener{(parentFragment as ModeDetailsFragment).setPanelMode(-1)}
        if(!panel.statesInitialized[INDEX])
            ApiService.getPanelState(panel){
                if(context != null) { // Check for context to be not null bc it's possible for user to back out of fragment before this executes, which causes crash
                    this.onPanelStateFetched()
                    panel.statesInitialized[INDEX] = true
                }
            }
    }

    /**
     * Callback that is called after the `start` function loads the panel's state
     * Implemented by individual mode fragments with logic for updating their UI with the newly loaded state data
     **/
    protected abstract fun onPanelStateFetched()
}
