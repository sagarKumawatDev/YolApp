package com.yol.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yol.R
import com.yol.databinding.ActivityCreateEventActitvityBinding
import com.yol.databinding.ActivityTermsAndConditionBinding
import com.yol.utils.Constants

class TermsAndCondition : AppCompatActivity() {
    private lateinit var binding: ActivityTermsAndConditionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.apply {
            webView.settings.javaScriptEnabled = true
            webView.loadUrl(Constants.TERMS_AND_CONDITION)

            /*iconBack.setOnClickListener {
                finish()
            }*/
        }
    }
}