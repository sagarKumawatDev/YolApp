package com.yol.ui.auth

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yol.databinding.ActivityRegisterBinding
import com.yol.network.request.RegisterRequest
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.vicmikhailau.maskededittext.MaskedFormatter
import com.vicmikhailau.maskededittext.MaskedWatcher
import android.net.wifi.WifiManager
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.format.Formatter
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.yol.R
import com.yol.model.User
import com.yol.ui.WelComeActivity
import com.yol.utils.*
import io.nlopez.smartlocation.SmartLocation
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Build
import android.widget.*
import androidx.annotation.RequiresApi
import com.yol.MainActivity
import com.yol.network.request.RegisterReq
import com.yol.ui.SelectTribesActivity
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.context.unloadKoinModules


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModel()
    private var formatter: MaskedFormatter? = null
    private var collegeList: ArrayList<String> = ArrayList()
    private var streamList: ArrayList<String> = ArrayList()
    private var location: Location? = null
    private var isForIndividual: Boolean = false
    val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  FacebookSdk.sdkInitialize(applicationContext);
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun init() {
        // authViewModel.getWebUser()
        authViewModel.getInstitute()
        authViewModel.getStream()
        observeValues()
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val ipAddress: String = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
        Log.d("Your Device IP Address:", ipAddress)
        SmartLocation.with(this).location()
            .oneFix()
            .start {
                location = it
            }

        binding.apply {
            txtSignIn.setOnClickListener { finish() }
            btnBack.setOnClickListener { finish() }
            editDate.setOnClickListener {
                DateTimePicker(this@RegisterActivity, false) {
                    val sdf = SimpleDateFormat(DateTimePicker.getFormat("d"), Locale.getDefault())
                    editDate.setText(sdf.format(it.calendar.time))
                }.show()
            }
            rlSingUp.setOnClickListener {

                if (isValid() && checkBox.isChecked) {
                    val user = RegisterReq(
                        college = if (isForIndividual) "" else etInstitute.text.toString(),
                        dob = editDate.text.toString(),
                        gender = autoCompleteTextView.text.toString(),
                        name = etName.text.toString().trim(),
                        stream = if (isForIndividual) "" else etStream.text.toString(),
                        mobile = etMobile.text.toString().trim(),
                        email = etEmail.text.toString().trim(),
                        password = password.text.toString().trim(),
                        country = ccp.selectedCountryName,
                        registration_type = registrationType.text.toString()
                    )

                    authViewModel.register(user)
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter valid details",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }


            // get reference to the string array that we just created
            val gender = resources.getStringArray(R.array.gender_list_and_type)
            // create an array adapter and pass the required parameter
            // in our case pass the context, drop down layout , and array.
            val arrayAdapter = ArrayAdapter(this@RegisterActivity, R.layout.dropdown_menu, gender)
            // get reference to the autocomplete text view
            // set adapter to the autocomplete tv to the arrayAdapter
            autoCompleteTextView.setAdapter(arrayAdapter)
            // autoCompleteTextView.setText(gender[0])

            val registrationTypes = resources.getStringArray(R.array.registration_type_list)
            val registrationTypesAdapter =
                ArrayAdapter(this@RegisterActivity, R.layout.dropdown_menu, registrationTypes)
            registrationType.setAdapter(registrationTypesAdapter)


            registrationType.onItemClickListener =
                AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                    if (position == 2) {
                        etStreamLayout.visibility = View.GONE
                        etInstituteLayout.visibility = View.GONE
                        isForIndividual = true
                    } else {
                        etStreamLayout.visibility = View.VISIBLE
                        etInstituteLayout.visibility = View.VISIBLE
                        isForIndividual = false
                    }
                }


        }

        authViewModel.registerState.observe(this@RegisterActivity, {
            Log.d("TAG", it.data.toString())
            when (it.status) {
                ApiState.Status.LOADING -> {
                    ProgressDialog.showProgressDialog(this)
                }
                ApiState.Status.SUCCESS -> {

                    it.data?.let { data ->
                        when (data) {
                            true -> {
                                unloadKoinModules(MyApplication.appModules)
                                loadKoinModules(MyApplication.appModules)
                                Thread.sleep(4000)
                                MyApplication.tinyDB.getString(Constants.SharedPref.LOGGED_IN_ACCESS_TOKEN)?.let { it1 ->
                                    authViewModel.getUserDetails(
                                        it1
                                    )
                                }
                            }
                            else -> {
//                                Toast.makeText(this, data.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                ApiState.Status.ERROR -> {
                    ProgressDialog.hideProgressDialog()
                    Toast.makeText(this, it.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        authViewModel.userDetails.observe(this, {
            Log.d("TAG", it.data.toString())
            when (it.status) {
                ApiState.Status.LOADING -> { // ProgressDialog.showProgressDialog(this)
                }
                ApiState.Status.SUCCESS -> {
                    ProgressDialog.hideProgressDialog()

                    it.data?.let { data ->

                        MyApplication.tinyDB.putBoolean(
                            Constants.SharedPref.LOGGGED_IN,
                            true
                        )
                        MyApplication.tinyDB.setUserDetails(data)


                        launchActivity(data.tribes)
                        finishAffinity()
                    }
                }
                ApiState.Status.ERROR -> {
                    ProgressDialog.hideProgressDialog()
                    Toast.makeText(this, it.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        setTermsAndConditionText()
    }
    private fun launchActivity(tribes: ArrayList<String>) {
        if (tribes.isNullOrEmpty()
        )
            startActivity(
                Intent(
                    this@RegisterActivity,
                    SelectTribesActivity::class.java
                )
            )
        else
            startActivity(
                Intent(
                    this@RegisterActivity,
                    MainActivity::class.java
                )
            )

    }

    private fun isValid(): Boolean {
        binding.apply {
            when {
                etName.text.isNullOrEmpty() -> return false
                !checkEmail(etEmail.text.toString()) -> return false
                password.text.isNullOrEmpty() -> return false
                editPinCode.text.isNullOrEmpty() -> return false
                etMobile.text.isNullOrEmpty() -> return false
                editDate.text.isNullOrEmpty() -> return false
                registrationType.text.isNullOrEmpty() -> return false
                checkBox.isChecked.not() -> false
            }
            if (isForIndividual.not()) {
                when {
                    etInstitute.text.isNullOrEmpty() -> return false
                    etStream.text.isNullOrEmpty() -> return false
                }
            }
        }
        return true
    }

    private fun observeValues() {

        authViewModel.institute.observe(this@RegisterActivity, {
            Log.d("TAG", it.data.toString())
            when (it.status) {
                ApiState.Status.LOADING -> {
                }
                ApiState.Status.SUCCESS -> {

                    collegeList = ArrayList<String>()
                    it.data?.let { list ->
                        for (college in list)
                            collegeList.add(college.institutename)

                        setAdapterForCollege()
                    }
                }
                ApiState.Status.ERROR -> {
                }
            }


        })
        authViewModel.stream.observe(this@RegisterActivity, {
            Log.d("TAG", it.data.toString())
            when (it.status) {

                ApiState.Status.LOADING -> {
                }
                ApiState.Status.SUCCESS -> {
                    streamList = ArrayList<String>()
                    it.data?.let { list ->
                        for (stream in list)
                            streamList.add(stream.streamname)

                        setAdapterForStream()

                    }

                }
                ApiState.Status.ERROR -> {
                }
            }
        })
    }


    private fun setAdapterForStream() {
        val adapter = ArrayAdapter(
            this@RegisterActivity, android.R.layout.simple_list_item_1, streamList
        )
        binding.etStream.setAdapter(adapter)
    }

    private fun setAdapterForCollege() {
        val adapter = ArrayAdapter<String>(
            this@RegisterActivity,
            android.R.layout.simple_list_item_1, collegeList
        )
        binding.apply {
            etInstitute.setAdapter(adapter)

        }
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun checkEmail(email: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }

    val clickableSpan = object : ClickableSpan() {
        override fun onClick(p0: View) {
            startActivity(Intent(this@RegisterActivity, TermsAndCondition::class.java))
        }
    }

    private fun setTermsAndConditionText() {
        val linkText =
            SpannableString("Check here to confirm our terms and privacy policy YOL Customer agreement")
        //SpannableString will be created with the full content and
        // the clickable content all together
        val spannableString =
            SpannableString("Check here to confirm our terms and privacy policy YOL Customer agreement")
        //only the word 'link' is clickable
        spannableString.setSpan(
            clickableSpan,
            linkText.indexOf("YOL"),
            linkText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        //The following is to set the new text in the TextView
        //no styles for an already clicked link
        binding.checkBox.text = spannableString
        binding.checkBox.movementMethod = LinkMovementMethod.getInstance()

    }
}
