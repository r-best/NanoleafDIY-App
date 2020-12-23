package com.example.nanoleafdiy.activities.palettes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.utils.PALETTE_PRESETS
import com.example.nanoleafdiy.utils.Palette
import com.example.nanoleafdiy.utils.PaletteColor
import com.pes.androidmaterialcolorpickerdialog.ColorPicker

class CreatePaletteActivity : AppCompatActivity() {
    private lateinit var listAdapter: ColorStepListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_palette)
    }

    override fun onStart() {
        super.onStart()

        listAdapter = ColorStepListAdapter(mutableListOf(PaletteColor(255, 255, 255, 500)))
        findViewById<RecyclerView>(R.id.palettecolor_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        findViewById<Button>(R.id.palettecolor_add_button).setOnClickListener {
            val dataset = listAdapter.getItems()
            dataset.add(PaletteColor(255, 255, 255, 500))
            listAdapter.notifyItemInserted(dataset.size-1)
            listAdapter.notifyItemRangeChanged(dataset.size-1, dataset.size)
        }

        findViewById<Button>(R.id.palettecolor_save_button).setOnClickListener {
            val name: String = findViewById<EditText>(R.id.palettecolor_name_input).text.toString()

            if(name == ""){
                Toast.makeText(this, "Palette must have a name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            for(palette in PALETTE_PRESETS){
                if(palette.name == name){
                    Toast.makeText(this, "A palette with this name already exists", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            PALETTE_PRESETS.add(Palette(name, listAdapter.getItems()))
            setResult(Activity.RESULT_OK, Intent())
            finish()
        }
    }
}

class ColorStepListAdapter(val dataset: MutableList<PaletteColor>): RecyclerView.Adapter<ColorStepListAdapter.PaletteColorViewHolder>(){
    private lateinit var parent: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaletteColorViewHolder {
        this.parent = parent
        return PaletteColorViewHolder(PaletteColorListViewItem(parent.context).apply {
            layoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            setPadding(0, 20, 0, 20)
            gravity = Gravity.CENTER
        })
    }

    override fun onBindViewHolder(holder: PaletteColorViewHolder, i: Int) {
        holder.itemLayoutView.colorView.setOnClickListener(onClickColor(i))
        holder.itemLayoutView.transitionView.addTextChangedListener(onUpdateDuration(i))
        holder.itemLayoutView.deleteButton.setOnClickListener(onClickDelete(i))
        holder.itemLayoutView.setPaletteColor(dataset[i])
    }

    fun getItems(): MutableList<PaletteColor> { return dataset }

    override fun getItemCount(): Int { return dataset.size }

    private fun onClickColor(i: Int): (View) -> Unit {
        return { v: View -> run {
            val step = dataset[i]
            val colorPicker = ColorPicker((parent.context as CreatePaletteActivity), step.r, step.g, step.b)
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

    class PaletteColorViewHolder(var itemLayoutView: PaletteColorListViewItem) : RecyclerView.ViewHolder(itemLayoutView)
}

class PaletteColorListViewItem : LinearLayout {
    var colorView: View
    var transitionView: EditText
    var deleteButton: Button

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes){
        inflate(context, R.layout.palettecolor_listview_item, this)
        colorView = findViewById(R.id.palettecolor_item_colorbox)
        transitionView = findViewById(R.id.palettecolor_item_transitiontime)
        deleteButton = findViewById(R.id.palettecolor_item_delete)
    }

    fun setPaletteColor(paletteColor: PaletteColor) {
        colorView.setBackgroundColor(Color.argb(255, paletteColor.r, paletteColor.g, paletteColor.b))
        transitionView.setText(paletteColor.t.toString())
    }
}