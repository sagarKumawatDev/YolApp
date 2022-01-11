package com.yol.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yol.databinding.*
import com.yol.model.EventDataModel
import com.yol.model.TribesModel
import com.yol.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SelectTribesAdapter(
    var mContext: Context,
    var list: ArrayList<TribesModel>,
    var onItemClickListener: OnItemClickListener,
) : RecyclerView.Adapter<SelectTribesAdapter.ViewHolder>() {

    inner class ViewHolder(val tribesListItemBinding: TribesListItemBinding) :
        RecyclerView.ViewHolder(tribesListItemBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(tribe: TribesModel, pos: Int) {

            tribesListItemBinding.apply {
                tribesName.text = tribe.name
                tribesMainCard.setOnClickListener {
                    onItemClickListener.onItemClick(list[pos], pos)
                }
                checkBox.isChecked = tribe.state
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val tribesListItemBinding =
            TribesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(tribesListItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position], position)


    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onItemClick(name: TribesModel, position: Int)
    }

}