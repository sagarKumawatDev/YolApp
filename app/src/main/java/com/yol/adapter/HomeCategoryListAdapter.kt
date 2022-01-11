package com.yol.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.yol.R
import com.yol.databinding.HomeCategoryListAdapterBinding
import com.yol.model.Mindmap
import com.yol.utils.Constants
import com.yol.viewmodel.MainActivityViewModel


class HomeCategoryListAdapter(
    var mContext: Context,
    var homeCategoryList: ArrayList<Mindmap>,
    var mainActivityViewModel: MainActivityViewModel,
    var onItemClickListener: HomeCategoryListAdapter.OnItemClickListener,
) : RecyclerView.Adapter<HomeCategoryListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeCategoryListAdapter.ViewHolder {
        val homeCategoryListAdapterBinding =
            HomeCategoryListAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(homeCategoryListAdapterBinding = homeCategoryListAdapterBinding)
    }

    override fun onBindViewHolder(holder: HomeCategoryListAdapter.ViewHolder, position: Int) {
        holder.bind(homeCategoryList[position], position)
    }

    override fun getItemCount(): Int = homeCategoryList.size


    inner class ViewHolder(var homeCategoryListAdapterBinding: HomeCategoryListAdapterBinding) :
        RecyclerView.ViewHolder(homeCategoryListAdapterBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(mapModel: Mindmap, pos: Int) {

            homeCategoryListAdapterBinding.apply {
                icon.setImageResource(Constants.categoryIcon[pos])
                name.text = mapModel.name.capitalize()
                total.text = "${mapModel.count} / ${mainActivityViewModel.TOTAL_POST_COUNT}"

                // set drawable tint
                ContextCompat.getDrawable(mContext, R.drawable.home_category_item_name_bg)?.apply {
                    DrawableCompat.wrap(this).apply {
                        setTint(getColorForMindMap(mapModel.days))
                        setTintMode(PorterDuff.Mode.SRC_IN)
                        rounded.setBackgroundDrawable(this)
                    }
                }
                rounded.setOnClickListener {
                    onItemClickListener.onItemClick(
                        mapModel.name, pos
                    )
                }
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(name: String, position: Int)
    }

    private fun getColorForMindMap(int: Int): Int {
        return when (int) {
            in 0..1 -> Color.parseColor("#68bd01")
            in 2..7 -> Color.parseColor("#FFFF00")
            in 7..21 -> Color.parseColor("#FF0000")
            else -> Color.parseColor("#818181")
        }
    }

}