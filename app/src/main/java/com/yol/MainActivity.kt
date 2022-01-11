package com.yol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yol.databinding.ActivityMainBinding

import java.util.*
import android.R.*
import android.content.Intent
import androidx.fragment.app.Fragment

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yol.ui.SelectTribesActivity
import com.yol.ui.event.EventFragment
import com.yol.ui.home.HomeFragment
import com.yol.ui.timeline.TimelineFragment
import com.yol.utils.Constants
import com.yol.utils.MyApplication
import com.yol.viewmodel.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainActivityViewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.apply {
            bottomNavigation.setOnNavigationItemSelectedListener(navListener)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()
        /*if ( MyApplication.tinyDB.getBoolean(Constants.SharedPref.LOGGGED_IN,false)) {
            if (MyApplication.tinyDB.getListString(Constants.SharedPref.USER_TIMBER_LIST)
                    .isNullOrEmpty()
            )
                startActivity(Intent(this, SelectTribesActivity::class.java))
            else


        }*/

    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item -> // By using switch we can easily get
            // the selected fragment
            // by using there id.
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.home -> selectedFragment = HomeFragment()
                R.id.timeline -> selectedFragment = TimelineFragment()
                R.id.event -> selectedFragment = EventFragment()
            }
            // It will help to replace the
            // one fragment to other.
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, selectedFragment!!)
                .commit()
            true
        }


}

