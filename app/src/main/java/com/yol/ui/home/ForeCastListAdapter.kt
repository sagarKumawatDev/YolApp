package com.yol.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.yol.databinding.ForecastLayoutItmeBinding

class ForeCastListAdapter(
    var mContext: Context,
    var list: List<ClapperBoardForecast>,
    var onItemClickListener: OnItemClickListener,
) : RecyclerView.Adapter<ForeCastListAdapter.ViewHolder>() {

    inner class ViewHolder(var forecastLayoutItmeBinding: ForecastLayoutItmeBinding) :
        RecyclerView.ViewHolder(forecastLayoutItmeBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(eventDataModel: String, pos: Int) {
            forecastLayoutItmeBinding.apply {
                icon.setImageResource(list[pos].idResource)
                name.text = list[pos].name
                rl.isClickable = true
                checked.isVisible = false
                rl.setOnClickListener {
                    rl.isClickable = false
                    checked.isVisible = true
                    onItemClickListener.onItemClick(list[pos], pos)
                    notifyDataSetChanged()

                }

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val forecastLayoutItmeBinding =
            ForecastLayoutItmeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(forecastLayoutItmeBinding = forecastLayoutItmeBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position].name, position)


    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onItemClick(name: ClapperBoardForecast, position: Int)
    }

}