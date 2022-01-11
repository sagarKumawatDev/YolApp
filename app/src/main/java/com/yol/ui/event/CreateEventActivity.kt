package com.yol.ui.event

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.yol.databinding.ActivityCreateEventActitvityBinding
import com.yol.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.yol.R
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.yol.model.LocationData
import android.location.Geocoder
import com.yol.network.request.CreateEventRequest


class CreateEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateEventActitvityBinding
    private val eventViewModel: EventViewModel by viewModel()
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private var locationData: LocationData = LocationData()
    private var startDateAndTimeApi: String = ""
    private var endDateAndTimeApi: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventActitvityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initLocationPicker()


    }

    private fun initLocationPicker() {
        if (!Places.isInitialized()) {
            Places.initialize(this@CreateEventActivity, getString(R.string.google_maps_key));
        }

        val placesClient = Places.createClient(this)

        val token = AutocompleteSessionToken.newInstance()

        // Create a RectangularBounds object.
        val bounds = RectangularBounds.newInstance(
            LatLng(-33.880490, 151.184363),
            LatLng(-33.858754, 151.229596)
        )
        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request =
            FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setOrigin(LatLng(-33.8749937, 151.2041382))
                .setCountries("IN")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(TypeFilter.ADDRESS.name)
                .build()
        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                for (prediction in response.autocompletePredictions) {
                    Log.i("", prediction.getPrimaryText(null).toString())
                }
            }.addOnFailureListener { exception: Exception? ->
                if (exception is ApiException) {

                }
            }


        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
            }

            override fun onError(status: Status) {
            }
        })
    }

    private fun init() {
        binding.apply {
            userName.text=MyApplication.tinyDB.getUserDetails()?.name?:""
            var check = true
            radioButton.setOnClickListener {
                when {
                    check -> {
                        radioButton.isChecked = false
                        check = false
                        radioButton.setText(R.string.public_text)
                    }
                    else -> {
                        radioButton.isChecked = true
                        check = true
                        radioButton.setText(R.string.private_text)
                    }
                }
            }

            startDateAndTime.setOnClickListener {
                DateTimePicker(this@CreateEventActivity, true) {
                    val sdf = SimpleDateFormat(DateTimePicker.getFormat("dt"), Locale.getDefault())
                    val apiF = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    startDateAndTimeApi = apiF.format(it.calendar.time)

                    startDateAndTime.text = sdf.format(it.calendar.time)
                }.show()
            }
            endDateAndTime.setOnClickListener {
                if (startDateAndTime.text.isNullOrBlank().not()) {
                    DateTimePicker(this@CreateEventActivity, true) {
                        val sdf = SimpleDateFormat(DateTimePicker.getFormat("dt"), Locale.getDefault())
                        val apiF = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        endDateAndTimeApi = apiF.format(it.calendar.time)

                        endDateAndTime.text = sdf.format(it.calendar.time)
                    }.show()
                }
            }

            iconBack.setOnClickListener {
                finish()
            }

            rlCreateEvent.setOnClickListener {
                if (isValid()) {
                    binding.apply {

                        val createEventRequest = CreateEventRequest(
                            eventName = eventName.text.toString(),
                            startDateTime = startDateAndTimeApi,
                            endDateTime = endDateAndTimeApi,
                            email = MyApplication.tinyDB.getUserDetails()?.email ?: "",
                            longitude = 28.5342,//locationData.longitude,
                            latitude =77.2094, //locationData.latitude,
                            city = locationData.cityName,
                            completeAddress = locationData.completeAddress,
                            minAge = eventMinAge.text.toString().toInt() ?:0,
                            maxAge = eventMaxAge.text.toString().toInt() ?:0,
                            distance = eventDistance.text.toString().toInt()?:0,
                            public = check.not(),
                            gender = autoCompleteTextView.text.toString()
                        )
                       eventViewModel.createEvent(createEventRequest)

                    }


                } else {
                    Toast.makeText(
                        this@CreateEventActivity,
                        "Please fill all details",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            eventLocation.setOnClickListener {
                openLocationPicker()
            }

            // get reference to the string array that we just created
            val gender = resources.getStringArray(R.array.gender_list)
            // create an array adapter and pass the required parameter
            // in our case pass the context, drop down layout , and array.
            val arrayAdapter =
                ArrayAdapter(this@CreateEventActivity, R.layout.dropdown_menu, gender)
            // get reference to the autocomplete text view
            // set adapter to the autocomplete tv to the arrayAdapter
            autoCompleteTextView.setAdapter(arrayAdapter)
            autoCompleteTextView.setSelection(0)

        }

        eventViewModel.createEvent.observe(this, {
            Log.d("TAG", it.data.toString())
            when (it.status) {
                ApiState.Status.LOADING -> {
                    ProgressDialog.showProgressDialog(this)
                }
                ApiState.Status.SUCCESS -> {
                    ProgressDialog.hideProgressDialog()

                    it.data?.let { data ->
                        when (data) {
                            true -> finish()

                            false -> {
                                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                                    .show()
                            }

                        }
                    }
                }
                ApiState.Status.ERROR -> {
                    ProgressDialog.hideProgressDialog()
                }
            }
        })


        //eventViewModel.getUserDetail()
    }

    private fun openLocationPicker() {
        val fields =
            listOf(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .setTypeFilter(TypeFilter.ADDRESS)
            .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)

    }

    private fun isValid(): Boolean {
        binding.apply {
            when {
                eventName.text.isNullOrEmpty() -> return false
                startDateAndTime.text.isNullOrEmpty() -> return false
                endDateAndTime.text.isNullOrEmpty() -> return false
                //eventLocation.text.isNullOrEmpty() -> return false
                eventMinAge.text.isNullOrEmpty() -> return false
                eventMaxAge.text.isNullOrEmpty() -> return false
                //eventGender.text.isNullOrEmpty() -> return false
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i("Selected Place", "Place: ${place.name}, ${place.id}")
                        locationData.latitude = place.latLng?.latitude ?: 0.0
                        locationData.longitude = place.latLng?.longitude ?: 0.0
                        locationData.completeAddress = place.address
                        val geocoder = Geocoder(this@CreateEventActivity, Locale.getDefault())
                        val addresses: List<Address> = geocoder.getFromLocation(
                            locationData.latitude?.toDouble() ?: 0.0,
                            locationData.longitude?.toDouble() ?: 0.0,
                            1
                        )
                        locationData.cityName = addresses[0].locality
                        binding.eventLocation.text = place.address
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i("Selected Place Error", status.statusMessage)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}