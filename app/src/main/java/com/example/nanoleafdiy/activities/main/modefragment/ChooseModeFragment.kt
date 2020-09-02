package com.example.nanoleafdiy.activities.main.modefragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.nanoleafdiy.activities.main.ModeDetailsFragment
import com.example.nanoleafdiy.utils.PANEL_MODES


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
        val parent = LinearLayout(context).apply{
            id = View.generateViewId()
            orientation = LinearLayout.VERTICAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
        for(i in PANEL_MODES.indices step 2){
            // Create the two buttons in this row (second is set to invisible if there's only 1 mode left)
            val text1 = TextView(context).apply {
                id = View.generateViewId()
                layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
                gravity = Gravity.CENTER
                text = PANEL_MODES[i].name
                setOnClickListener{ (parentFragment as ModeDetailsFragment).setPanelMode(i) }
            }
            val text2 = TextView(context).apply {
                id = View.generateViewId()
                layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
                gravity = Gravity.CENTER
                if(i+1 < PANEL_MODES.size) {
                    text = PANEL_MODES[i+1].name
                    setOnClickListener{ (parentFragment as ModeDetailsFragment).setPanelMode(i+1) }
                }
                else
                    visibility = View.INVISIBLE
            }

            // Put the two buttons in a row and add that row to the screen
            parent.addView(LinearLayout(context).apply {
                id = View.generateViewId()
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f)
                addView(text1)
                addView(text2)
            })
        }

        return parent
    }
}
