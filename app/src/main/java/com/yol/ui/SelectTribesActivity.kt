package com.yol.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.yol.MainActivity
import com.yol.R
import com.yol.adapter.SelectTribesAdapter
import com.yol.databinding.ActivitySelectTribesBinding
import com.yol.model.TribesModel
import com.yol.ui.auth.AuthViewModel
import com.yol.utils.ApiState
import com.yol.utils.Constants
import com.yol.utils.MyApplication
import com.yol.utils.ProgressDialog
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.koin.androidx.viewmodel.ext.android.viewModel
import okhttp3.RequestBody
import org.json.JSONArray
import java.lang.Thread.sleep


class SelectTribesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectTribesBinding
    private val tribesList = ArrayList<TribesModel>()
    private var selectedTribesList = arrayListOf<String>()
    private var selectedTribesListJSONArray = JSONArray()
    private var selectTribeAdapter: SelectTribesAdapter? = null
    private val authViewModel: AuthViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectTribesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    fun init() {
        tribesList.add(TribesModel("Outings and adventure"))
        tribesList.add(TribesModel("Cycling"))
        tribesList.add(TribesModel("Reading and/or Writing"))
        tribesList.add(TribesModel("Music and cinema"))
        tribesList.add(TribesModel("Cooking and fine-dine"))
        tribesList.add(TribesModel("Hang outs"))
        tribesList.add(TribesModel("Driving"))
        tribesList.add(TribesModel("Biking "))
        tribesList.add(TribesModel("Academic and Tech"))
        tribesList.add(TribesModel("Psychology"))
        tribesList.add(TribesModel("Fine arts"))
        tribesList.add(TribesModel("Architecture"))
        tribesList.add(TribesModel("Gaming"))
        tribesList.add(TribesModel("Any other"))

        selectTribeAdapter = SelectTribesAdapter(
            this@SelectTribesActivity,
            tribesList,
            object : SelectTribesAdapter.OnItemClickListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onItemClick(tribe: TribesModel, position: Int) {
                    if (selectedTribesList.size < 5 || tribe.state) {
                        if (tribe.state) {
                            selectedTribesList.remove(tribe.name)
                            tribe.state = false
                            tribesList[position] = tribe
                            selectTribeAdapter?.notifyDataSetChanged()
                            if (position == tribesList.lastIndex)
                                binding.tribeFieldtl.visibility = View.GONE
                        } else {
                            selectedTribesList.add(tribe.name).toString()
                            tribe.state = true
                            tribesList[position] = tribe
                            selectTribeAdapter?.notifyDataSetChanged()
                            if (position == tribesList.lastIndex)
                                binding.tribeFieldtl.visibility = View.VISIBLE
                        }

                    } else {
                        Toast.makeText(
                            this@SelectTribesActivity,
                            "can not select more than 5 Tribes",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            })

        binding.apply {
            closeButton.setOnClickListener {
                finish()
            }
            rlDone.setOnClickListener {
                if (selectedTribesList.size != 0) {
                    selectedTribesListJSONArray = JSONArray(selectedTribesList)
                    selectedTribesList.add(binding?.tribeField.text?.trim().toString())
                    val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
                    authViewModel.updateTribes(
                        RequestBody.create(
                            mediaType,
                            selectedTribesListJSONArray.toString()
                        )
                    )


                } else {
                    Toast.makeText(
                        this@SelectTribesActivity,
                        "Select at least one tribe",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }


            tribesRv.apply {
                adapter = selectTribeAdapter
                layoutManager = LinearLayoutManager(this@SelectTribesActivity)
                setHasFixedSize(true)
            }
        }
        authViewModel.updateTribes.observe(this, {
            Log.d("TAG", it.data.toString())
            when (it.status) {

                ApiState.Status.LOADING -> {
                    ProgressDialog.showProgressDialog(this)

                }
                ApiState.Status.SUCCESS -> {
                    ProgressDialog.hideProgressDialog()

                    it.data?.let { data ->
                        val userDetails = MyApplication.tinyDB.getUserDetails()
                        userDetails?.tribes = data.tribes
                        MyApplication.tinyDB.setUserDetails(userDetails)
                        sleep(1000)
                        startActivity(Intent(this@SelectTribesActivity, MainActivity::class.java))
                        finish()
                    }
                }
                ApiState.Status.ERROR -> {
                    ProgressDialog.hideProgressDialog()

                }
            }
        })

    }
}