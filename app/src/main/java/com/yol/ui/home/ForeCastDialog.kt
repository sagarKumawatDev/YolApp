package com.yol.ui.home

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.yol.R
import com.yol.databinding.FragmentForecastDialogBinding
import com.yol.network.ForecastModelRequest
import com.yol.viewmodel.MainActivityViewModel


class ForeCastDialog(
    var mContext: Activity,
    var mainActivityViewModel: MainActivityViewModel,
    var isToday: Boolean
) : DialogFragment(),
    ForeCastListAdapter.OnItemClickListener {
    var binding: FragmentForecastDialogBinding? = null
    var arraylist: ArrayList<ClapperBoardForecast> = ArrayList()
    var todayID: Int? = null
    var yesterdayID: Int? = null

    companion object {

        const val TAG = "SimpleDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(
            title: String,
            isToday: Boolean,
            subTitle: String,
            mContext: Activity,
            mainActivityViewModel: MainActivityViewModel
        ): ForeCastDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            val fragment = ForeCastDialog(
                mContext = mContext,
                mainActivityViewModel = mainActivityViewModel,
                isToday = isToday
            )
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForecastDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupClickListeners()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupView() {
        binding?.apply {
            textView11.text = arguments?.getString(KEY_TITLE)
            setList()

        }
    }

    private fun setupClickListeners() {
        binding?.imageView3?.setOnClickListener {
            dismiss()
        }
    }

    private fun setList() {
        arraylist.add(ClapperBoardForecast(0, "green", "Happy & Productive", R.drawable.happy))
        arraylist.add(ClapperBoardForecast(1, "yellow", "Lazy or Busy", R.drawable.lazy))
        arraylist.add(ClapperBoardForecast(2, "red", "Not Good", R.drawable.man_thinking_copy_2))
        arraylist.add(ClapperBoardForecast(3, "gray", "Life Missed", R.drawable.gray_icon))
        var foreCastListAdapter = ForeCastListAdapter(mContext, arraylist, this)
        binding?.list?.adapter = foreCastListAdapter
    }

    override fun onItemClick(clapperBoardForecast: ClapperBoardForecast, position: Int) {
        mContext.runOnUiThread {
            when (isToday) {
                true -> {
                    binding?.textView11?.text = "How was my yesterday?"
                    todayID = clapperBoardForecast.id
                    isToday = false

                }
                false -> {
                    binding?.textView11?.text = "My Forecast for today?"
                    yesterdayID = clapperBoardForecast.id
                    isToday = true
                }

            }
            if (todayID != null && yesterdayID != null) {
                var forecastModel = ForecastModelRequest()
                forecastModel.today = todayID.toString()
                forecastModel.yesterday = yesterdayID.toString()
                mainActivityViewModel.putForecast(forecastModelRequest = forecastModel)
                dismiss()
            }
        }


    }

}

