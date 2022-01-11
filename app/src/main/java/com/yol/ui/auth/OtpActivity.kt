package com.yol.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.yol.databinding.ActivityOtpBinding
import com.yol.network.request.SendOtpRequest
import com.yol.utils.ApiState
import com.yol.utils.ProgressDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private val authViewModel: AuthViewModel by viewModel()
    private var state: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Forgot Password"
        binding.apply {
            rlSingIn.setOnClickListener {
                if (!etMobile.text.isNullOrEmpty()) {
                    authViewModel.sendOtp(SendOtpRequest(mobileno = etMobile.text.toString()))
                } else {
                    Toast.makeText(this@OtpActivity, "please enter mobile number", Toast.LENGTH_LONG).show()

                }

            }
            rlVerify.setOnClickListener {
                if (!otpView.text.isNullOrEmpty()) {
                    authViewModel.verifyOtp(
                        SendOtpRequest(
                            mobileno = etMobile.text.toString(),
                            otp = otpView.text.toString()
                        )
                    )
                } else {
                    Toast.makeText(this@OtpActivity, "please enter valid otp code", Toast.LENGTH_LONG).show()

                }
            }
        }
        authViewModel.sendOtp.observe(this, {
            Log.d("TAG", it.data.toString())
            when (it.status) {
                ApiState.Status.LOADING -> {
                    ProgressDialog.showProgressDialog(this)
                }
                ApiState.Status.SUCCESS -> {
                    ProgressDialog.hideProgressDialog()

                    it.data?.let { data ->
                        when (data.status) {
                            true -> {
                                binding.rlSendOtp.isVisible = false
                                binding.rlVerifyOtp.isVisible = true
                                state = true
                                Toast.makeText(this, data.message, Toast.LENGTH_LONG).show()
                            }
                            false -> {
                                Toast.makeText(this, data.message, Toast.LENGTH_LONG).show()
                            }

                        }

                    }
                }
                ApiState.Status.ERROR -> {
                    ProgressDialog.hideProgressDialog()
                }
            }
        })

        authViewModel.verifyOtp.observe(this, {
            Log.d("TAG", it.data.toString())
            when (it.status) {
                ApiState.Status.LOADING -> {
                    ProgressDialog.showProgressDialog(this)
                }
                ApiState.Status.SUCCESS -> {
                    ProgressDialog.hideProgressDialog()

                    it.data?.let { data ->
                        when (data.status) {
                            true -> {
                                startActivity(Intent(this, ChangePassword::class.java)
                                    .putExtra("mobile",binding.etMobile.text.toString()))
                                finish()

                                Toast.makeText(this, data.message, Toast.LENGTH_LONG).show()
                            }
                            false -> {
                                Toast.makeText(this, data.message, Toast.LENGTH_LONG).show()
                            }

                        }

                    }
                }
                ApiState.Status.ERROR -> {
                    ProgressDialog.hideProgressDialog()
                }
            }
        })


    }

    override fun onSupportNavigateUp(): Boolean {
        when (state) {
            true -> {
                binding.rlSendOtp.isVisible = true
                binding.rlVerifyOtp.isVisible = false
                state = false
            }
            false -> {
                super.onBackPressed()
            }
        }
        return true
    }

    override fun onBackPressed() {
        when (state) {
            true -> {
                binding.rlSendOtp.isVisible = true
                binding.rlVerifyOtp.isVisible = false
                state = false
            }
            false -> {
                super.onBackPressed()
            }
        }
    }


}