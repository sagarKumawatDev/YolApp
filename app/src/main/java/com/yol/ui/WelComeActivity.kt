package com.yol.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.yol.databinding.ActivityWelComeBinding
import com.yol.ui.auth.AuthViewModel
import com.yol.ui.auth.SplashScreen
import com.yol.utils.ApiState
import com.yol.utils.Constants
import com.yol.utils.MyApplication
import com.yol.viewmodel.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelComeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelComeBinding
    private var mAuth: FirebaseAuth? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelComeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    @SuppressLint("SetTextI18n")
    fun init() {
        mAuth = FirebaseAuth.getInstance()
        binding.apply {


            logout.setOnClickListener {
                MyApplication.tinyDB.putBoolean(
                    Constants.SharedPref.LOGGGED_IN,
                    false
                )
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@WelComeActivity, SplashScreen::class.java))
            }


        }
    }
}