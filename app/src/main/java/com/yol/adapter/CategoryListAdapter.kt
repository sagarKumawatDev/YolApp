package com.yol.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yol.databinding.EventListItemDesignBinding
import com.yol.databinding.TimelinePostCategoryBindingImpl
import com.yol.databinding.TimelinePostCategoryItmeBinding
import com.yol.model.EventDataModel
import com.yol.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CategoryListAdapter(
    var mContext: Context,
    var list: List<String>,
    var  onItemClickListener: OnItemClickListener,
    ) : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    inner class ViewHolder(var timelinePostCategoryItmeBinding: TimelinePostCategoryItmeBinding) :
        RecyclerView.ViewHolder(timelinePostCategoryItmeBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(eventDataModel: String, pos: Int) {

            timelinePostCategoryItmeBinding.apply {
                tvName.text = list[pos]
                icon.setImageResource(Constants.categoryIcon[pos])
                rootID.setOnClickListener {
                    onItemClickListener.onItemClick(list[pos],pos)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val timelinePostCategoryItmeBinding =
            TimelinePostCategoryItmeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(timelinePostCategoryItmeBinding = timelinePostCategoryItmeBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position], position)


    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onItemClick(name: String, position: Int)
    }

}