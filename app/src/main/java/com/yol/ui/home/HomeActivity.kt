package com.yol.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.setNestedScrollingEnabled
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.github.mikephil.charting.data.Entry
import com.yol.R
import com.yol.adapter.HomeCategoryListAdapter
import com.yol.databinding.ActivityHomeAcitityBinding
import com.yol.viewmodel.MainActivityViewModel
import org.eazegraph.lib.models.PieModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.github.mikephil.charting.utils.ColorTemplate

import com.github.mikephil.charting.data.PieData

import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import com.yol.model.Mindmap
import com.yol.model.Mindshare
import com.yol.ui.timeline.CreateTimeLineActivity
import com.yol.utils.*
import kotlin.time.ExperimentalTime


var mapD = 0.5f
var mindPresent = 1000f/12
const val mindShare40= 39f
const val mindShare30= 29f
const val mindShare20= 19f
const val mindShare10= 9.8f
const val defaultSlice= 1f

class HomeActivity : AppCompatActivity(), OnChartValueSelectedListener{
    private lateinit var binding: ActivityHomeAcitityBinding
    private val mainActivityViewModel: MainActivityViewModel by viewModel()
    var list = ArrayList<Mindmap>()
    var mindshare = ArrayList<Mindshare>()
    var homeCategoryListAdapter: HomeCategoryListAdapter? = null
    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_acitity)
        binding = ActivityHomeAcitityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    @ExperimentalTime
    private fun init() {
        mainActivityViewModel.getMindMap()



        mainActivityViewModel.map.observe(this, { it ->
            Log.d("TAG", it.data.toString())
            when (it.status) {
                ApiState.Status.LOADING -> {
                    ProgressDialog.showProgressDialog(this)
                }
                ApiState.Status.SUCCESS -> {
                    ProgressDialog.hideProgressDialog()
                   it.data?.let {
                       list.clear()
                       list.addAll(it.mindmap)
                       mindshare.clear()
                       mindshare.addAll(it.mindshare)
                       updateUi(intent.getIntExtra("key",0))
                       homeCategoryListAdapter?.notifyDataSetChanged()
                   }




                }
                ApiState.Status.ERROR -> {
                    ProgressDialog.hideProgressDialog()
                }
            }
        })

        binding.apply {

            when(MyApplication.tinyDB.getInt(Constants.SharedPref.USER_FOCECAST_STATUS,0)){
                0  -> rlCaver.setBackgroundResource(R.drawable.clapper_close)
                1  -> rlCaver.setBackgroundResource(R.drawable.green)
                2  -> rlCaver.setBackgroundResource(R.drawable.gray)
            }

            homeCategoryListAdapter = HomeCategoryListAdapter(
                this@HomeActivity,
                list,
                mainActivityViewModel = mainActivityViewModel,
                object : HomeCategoryListAdapter.OnItemClickListener {
                    override fun onItemClick(name: String, position: Int) {
                          startActivity(Intent(this@HomeActivity,CreateTimeLineActivity::class.java))
                    }
                })
            categoryRV.apply {
                adapter = homeCategoryListAdapter
                layoutManager = GridLayoutManager(this@HomeActivity, 2)
                isNestedScrollingEnabled = true;
            }
         //   ViewCompat.setNestedScrollingEnabled(categoryRV, false);

            iconBack.setOnClickListener {
                onBackPressed()
            }
            calender.setOnClickListener {
                DateTimePicker(this@HomeActivity, false) {
                    val sdf = SimpleDateFormat(DateTimePicker.getFormat("d"), Locale.getDefault())
                    //   startDateAndTime.text = sdf.format(it.calendar.time)
                }.show()
            }

            tab1.setOnClickListener {
                updateUi(0)
            }
            tab2.setOnClickListener {
                updateUi(1)
            }

        }

    }


    override fun onResume() {
        super.onResume()

    }


    private fun setDataForMindMap(int: Int) {

        binding.apply {
            val NoOfEmp = ArrayList<PieEntry>()
            var color = ArrayList<Int>()
           when(int) {
              0 -> {
                  list.mapIndexed { idx, value ->
                      color.add(getColorForMindMap(value.days))
                      NoOfEmp.add(PieEntry(mindPresent, idx.toString()))

                  }
              }
              1 ->{
                  color.add(Color.GREEN)
                  color.add(Color.YELLOW)
                  color.add(Color.RED)
                  color.add(Color.GRAY)

                  mindshare.mapIndexed { idx, value ->
                    when(idx){
                        0 -> NoOfEmp.add(PieEntry(40f, "40%"))
                        1 -> NoOfEmp.add(PieEntry(30f, "30%"))
                        2 -> NoOfEmp.add(PieEntry(20f, "20%"))
                        3 -> NoOfEmp.add(PieEntry(10f, "10%"))
                        else -> Unit
                    }

                  }
              }

           }

            val dataSet = PieDataSet(NoOfEmp, "Mind Map")

            dataSet.setDrawIcons(false)
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0F, 40F)
            dataSet.selectionShift = 5f
            dataSet.colors = color
            pieChart.setDrawEntryLabels(true);
            pieChart.setOnChartValueSelectedListener(this@HomeActivity)

            val data = PieData(dataSet)
            pieChart.data = data
            data.setDrawValues(false)

            pieChart.setDrawSliceText(true); // To remove slice text
            pieChart.setEntryLabelTextSize(10f) // To remove slice text
            pieChart.setEntryLabelColor(Color.WHITE) // To remove slice text
            pieChart.setDrawMarkers(false); // To remove markers when click
            pieChart.description.isEnabled = false; // To remove description of pie
            pieChart.legend.isEnabled = false;
//            pieChart.m


            pieChart.highlightValues(null)

            pieChart.invalidate()
            pieChart.animateXY(1500, 1500)

        }
    }

    private fun getColorForMindMap(int: Int): Int {
        return when (int) {
            in 0..1 -> Color.parseColor("#68bd01")
            in 2..7 -> Color.parseColor("#FFFF00")
            in 7..21 -> Color.parseColor("#FF0000")
            else -> Color.parseColor("#818181")
        }
    }
    private fun getColorForMinShare(int: Int): Int {
        return when (int) {
            1 -> Color.parseColor("#68bd01")
            2 -> Color.parseColor("#FFFF00")
            3 -> Color.parseColor("#FF0000")
            else -> Color.parseColor("#818181")
        }
    }

    @ExperimentalTime
    fun updateUi(int: Int){
        binding.apply {
            when (int) {
                0 -> {
                    tab1.setBackgroundResource(R.drawable.upcoming_round_bg);
                    tab1.setTextColor(resources.getColor(R.color.white))
                    tab2.setBackgroundResource(R.drawable.past_round_bg);
                    tab2.setTextColor(resources.getColor(R.color.textColor))
                    mapData.isVisible = true
                    pieChart.isVisible = true
                    setDataForMindMap(0)
                }
                1 -> {
                    tab2.setBackgroundResource(R.drawable.upcoming_round_bg);
                    tab2.setTextColor(resources.getColor(R.color.white))
                    tab1.setBackgroundResource(R.drawable.past_round_bg);
                    tab1.setTextColor(resources.getColor(R.color.textColor))
                    mapData.isVisible = false
                    pieChart.isVisible = true
                    setDataForMindMap(1)

                }
            }
        }

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null)
            return;
        Log.i("VAL SELECTED", "Value: " + e.y + ", index: " + h?.x + ", DataSet index: " + h?.dataSetIndex);

        h?.x?.toInt().let {
            if (it != null) {
                binding.categoryRV.scrollToPosition(it)

            }
        }

    }

    override fun onNothingSelected() {

    }

}


