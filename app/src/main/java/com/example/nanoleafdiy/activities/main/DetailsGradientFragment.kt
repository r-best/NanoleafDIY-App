package com.example.nanoleafdiy.activities.main

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.databinding.FragmentDetailsGradientBinding
import com.example.nanoleafdiy.utils.ApiService
import com.example.nanoleafdiy.utils.GradientStep
import com.pes.androidmaterialcolorpickerdialog.ColorPicker


/**
 * A simple [Fragment] subclass.
 * Use the [DetailsGradientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsGradientFragment: DetailsSettingsFragmentBase {
    override val INDEX: Int = 1

    constructor() : super()
    constructor(directions: String): super(directions)

    // Copy of the panel's gradientSteps field, to be edited and assigned back to the panel when the users hits 'Confirm'
    private lateinit var changes: MutableList<GradientStep>

    private lateinit var listAdapter: GradientSetAdapter

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

        resetChanges()

        (context as MainActivity).findViewById<RecyclerView>(R.id.gradient_step_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        (context as MainActivity).findViewById<Button>(R.id.gradient_add_button).setOnClickListener {
            changes.add(GradientStep(255, 255, 255, 500))
            listAdapter.notifyItemInserted(changes.size-1)
            listAdapter.notifyItemRangeChanged(changes.size-1, changes.size)
        }

        (context as MainActivity).findViewById<Button>(R.id.gradient_confirm_button).setOnClickListener {
            panel.gradientSteps = changes
            ApiService.setGradient(panel)
        }

        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        resetChanges()
    }

    private fun resetChanges(){
        changes = mutableListOf()

        for(gradientStep in panel.gradientSteps)
            changes.add(GradientStep(gradientStep.r, gradientStep.g, gradientStep.b, gradientStep.t))
        listAdapter = GradientSetAdapter(changes)

        (context as MainActivity).findViewById<RecyclerView>(R.id.gradient_step_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }
    }
}

class GradientSetAdapter(private val dataset: MutableList<GradientStep>): RecyclerView.Adapter<GradientSetAdapter.GradientViewHolder>(){
    private lateinit var parent: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradientViewHolder {
        this.parent = parent
        return GradientViewHolder(GradientItemView(parent.context))
    }

    override fun onBindViewHolder(holder: GradientViewHolder, i: Int) {
        holder.itemLayoutView.colorView.setOnClickListener(onClickColor(i))
        holder.itemLayoutView.transitionView.addTextChangedListener(onUpdateDuration(i))
        holder.itemLayoutView.deleteButton.setOnClickListener(onClickDelete(i))
        holder.itemLayoutView.setGradientStep(dataset[i])
    }

    override fun getItemCount(): Int { return dataset.size }

    private fun onClickColor(i: Int): (View) -> Unit {
        return { v: View -> run {
            val step = dataset[i]
            val colorPicker = ColorPicker((parent.context as MainActivity), step.r, step.g, step.b)
            colorPicker.show()
            colorPicker.findViewById<Button>(R.id.okColorButton).setOnClickListener(fun(_: View) {
                step.r = colorPicker.red
                step.g = colorPicker.green
                step.b = colorPicker.blue
                v.setBackgroundColor(Color.argb(255, step.r, step.g, step.b))
                colorPicker.dismiss()
            })
        }}
    }

    private fun onUpdateDuration(i: Int): TextWatcher {
        return object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){
                if(i < dataset.size) // Check size bc sometimes when deleting an item, this method is called on what was previously the last item in the list, causing an index out of bounds error
                    dataset[i].t = s.toString().toInt()
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int){}
            override fun afterTextChanged(s: Editable){}
        }
    }

    private fun onClickDelete(i: Int): (View) -> Unit {
        return { _: View -> run {
            dataset.removeAt(i)
            notifyItemRemoved(i)
            notifyItemRangeChanged(i, dataset.size)
        }}
    }

    class GradientViewHolder(var itemLayoutView: GradientItemView) : RecyclerView.ViewHolder(itemLayoutView)
}

class GradientItemView : LinearLayout {
    var colorView: View
    var transitionView: EditText
    var deleteButton: Button

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes){
        inflate(context, R.layout.gradient_listview_item, this)
        colorView = findViewById(R.id.gradient_item_colorbox)
        transitionView = findViewById(R.id.gradient_item_transitiontime)
        deleteButton = findViewById(R.id.gradient_item_delete)
    }

    fun setGradientStep(gradientStep: GradientStep){
        colorView.setBackgroundColor(Color.argb(255, gradientStep.r, gradientStep.g, gradientStep.b))
        transitionView.setText(gradientStep.t.toString())
    }
}
