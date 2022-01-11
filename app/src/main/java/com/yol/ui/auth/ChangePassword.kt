package com.yol.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.yol.databinding.ActivityChangePasswordBinding
import com.yol.network.request.SendOtpRequest
import com.yol.utils.ApiState
import com.yol.utils.ProgressDialog

import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePassword : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Change Password"
        binding.apply {
            binding.rlSingIn.setOnClickListener {
                if (!newPassword.text.toString().isNullOrEmpty()) {
                    if (newPassword.text.toString().equals(confirmPassword.text.toString())) {
                        intent.getStringExtra("mobile")?.let { it1 -> SendOtpRequest(mobileno = it1,password = newPassword.text.toString()) }?.let { it2 ->
                            authViewModel.changePassword(it2)
                        }
                    } else {
                        Toast.makeText(this@ChangePassword, "confirm password does not match", Toast.LENGTH_LONG).show()

                    }

                } else {
                    Toast.makeText(this@ChangePassword, "please enter new Password", Toast.LENGTH_LONG).show()

                }
            }
        }
        authViewModel.changePassword.observe(this, {
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
        onBackPressed()
        return true
    }
}