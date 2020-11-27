package com.example.nanoleafdiy.activities.presets

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nanoleafdiy.*
import com.example.nanoleafdiy.utils.*


class PresetsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presets)
    }

    override fun onStart() {
        super.onStart()

        findViewById<RecyclerView>(R.id.presetcolor_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = PresetListAdapter(PALETTE_PRESETS)
        }
    }
}

class PresetListAdapter(private val dataset: List<Palette>): RecyclerView.Adapter<PresetListAdapter.PresetViewHolder>(){
    private lateinit var parent: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresetViewHolder {
        this.parent = parent
        return PresetViewHolder(TextView(parent.context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setPadding(0, 20, 0, 20)
            gravity = Gravity.CENTER
            textSize = 20f
        })
    }

    override fun onBindViewHolder(holder: PresetViewHolder, i: Int) {
        holder.itemLayoutView.text = dataset[i].name
        holder.itemLayoutView.setOnClickListener { v: View -> run {
            (v.context as PresetsActivity).setResult(RESULT_OK, Intent().apply { putExtra("preset_index", i) })
            (v.context as PresetsActivity).finish()
        }}
    }

    override fun getItemCount(): Int { return dataset.size }

    class PresetViewHolder(var itemLayoutView: TextView) : RecyclerView.ViewHolder(itemLayoutView)
}
