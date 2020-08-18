package com.example.nanoleafdiy.activities.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.databinding.ViewDataBinding
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.databinding.FragmentDetailsSolidBinding
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
    protected abstract val INDEX: Int // Overloaded in subclasses with subclass's index number (0 for solid, 1 for gradient, etc..)
    protected lateinit var binding: ViewDataBinding
    protected lateinit var panel: Panel

    protected fun create(inflater: LayoutInflater, resource: Int, container: ViewGroup?){
        panel = getPanel(arguments?.getString("directions")!!)!!
        binding = DataBindingUtil.inflate(inflater, resource, container, false)
    }

    protected fun start(resource: Int) {
        (context as MainActivity).findViewById<Button>(resource).setOnClickListener{(parentFragment as DetailsFragment).setPanelMode(-1)}
        if(!panel.statesInitialized[INDEX])
            ApiService.getPanelState(panel){
                (context as MainActivity).redrawDiagram()
                if(INDEX == 0) (binding as FragmentDetailsSolidBinding).invalidateAll()
            }
    }

    fun invalidateBindings(){
        binding.invalidateAll()
    }
}
