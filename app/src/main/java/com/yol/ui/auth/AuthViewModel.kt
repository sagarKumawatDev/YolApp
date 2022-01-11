package com.yol.ui.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.yol.di.Resource
import com.yol.network.repositories.AuthRepository
import com.yol.network.request.LoginRequest
import com.yol.network.request.RegisterReq
import com.yol.network.request.SendOtpRequest
import com.yol.network.response.Institute
import com.yol.network.response.Streams
import com.yol.network.response.SuccessResponse
import com.yol.network.response.UserProfileDetails
import com.yol.utils.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody


class AuthViewModel(
    private val authRepository: AuthRepository,
    private val firebaseAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference,
) : ViewModel() {

    var stream = MutableLiveData<ApiState<ArrayList<Streams>>>()
    private val _stream: ApiState<ArrayList<Streams>> =
        ApiState(ApiState.Status.LOADING, null, null)


    var institute = MutableLiveData<ApiState<ArrayList<Institute>>>()
    private val _institute: ApiState<ArrayList<Institute>> =
        ApiState(ApiState.Status.LOADING, null, null)

    val usersReference = databaseReference.child("users")
    var userDetails = MutableLiveData<ApiState<UserProfileDetails>>()
    private val _userDetails: ApiState<UserProfileDetails> =
        ApiState(ApiState.Status.LOADING, null, null)


    var loginState = MutableLiveData<ApiState<Boolean>>()
    private val _loginState: ApiState<Boolean> =
        ApiState(ApiState.Status.LOADING, null, null)

    var registerState = MutableLiveData<ApiState<Boolean>>()
    private val _registerState: ApiState<Boolean> =
        ApiState(ApiState.Status.LOADING, null, null)

    var sendOtp = MutableLiveData<ApiState<SuccessResponse>>()
    private val _sendOto: ApiState<SuccessResponse> =
        ApiState(ApiState.Status.LOADING, null, null)

    var verifyOtp = MutableLiveData<ApiState<SuccessResponse>>()
    private val _verifyOtp: ApiState<SuccessResponse> =
        ApiState(ApiState.Status.LOADING, null, null)

    var changePassword = MutableLiveData<ApiState<SuccessResponse>>()
    private val _changePassword: ApiState<SuccessResponse> =
        ApiState(ApiState.Status.LOADING, null, null)

    var updateTribes = MutableLiveData<ApiState<UserProfileDetails>>()
    private val _updateTribes: ApiState<UserProfileDetails> =
        ApiState(ApiState.Status.LOADING, null, null)

    //login
    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            runCatching {
                loginState.value = _loginState.loading()
                authRepository.login(loginRequest = loginRequest)
            }.onSuccess {
                when (it) {
                    is Resource.Success -> {
                        MyApplication.tinyDB.putString(
                            Constants.SharedPref.LOGGED_IN_ACCESS_TOKEN,
                            it.value.data?.accessToken
                        )
                        loginState.value = _loginState.success(it.value.success)

                    }
                    is Resource.Failure -> {
                        ProgressDialog.hideProgressDialog()
                        handleApiError(failure = it)

                        // loginState.value = _loginState.error(it.message)

                    }
                    else -> Unit
                }

            }.onFailure {
                loginState.value = _loginState.error(it)
            }
        }
    }

    //register
    fun register(registerRequest: RegisterReq) {
        viewModelScope.launch {
            runCatching {
                registerState.value = _registerState.loading()
                authRepository.register(registerRequest = registerRequest)
            }.onSuccess {
                when (it) {
                    is Resource.Success -> {
                        MyApplication.tinyDB.putString(
                            Constants.SharedPref.LOGGED_IN_ACCESS_TOKEN,
                            it.value.data?.accessToken
                        )
                        registerState.value = _registerState.success(it.value.success)

                    }
                    is Resource.Failure -> {
                        ProgressDialog.hideProgressDialog()
                        handleApiError(failure = it)
                    }
                    else -> Unit
                }
            }.onFailure {
                registerState.value = _registerState.error(it)
            }
        }
    }

    //_Institute
    fun getInstitute() {
        viewModelScope.launch {
            runCatching {
                institute.value = _institute.loading()
                authRepository.getInstitute()
            }.onSuccess {
                when (it) {
                    is Resource.Success -> {
                        institute.value = _institute.success(it.value.data)
                    }
                    is Resource.Failure -> {
                        //MyApplication.toast(it.message)
                    }
                    else -> Unit
                }

            }.onFailure {
                institute.value = _institute.error(it)
            }
        }
    }

    //register
    fun getStream() {
        viewModelScope.launch {
            runCatching {
                stream.value = _stream.loading()
                authRepository.getStream()
            }.onSuccess {
                when (it) {
                    is Resource.Success -> {
                        stream.value = _stream.success(it.value.data)
                    }
                    is Resource.Failure -> {
                        //MyApplication.toast(it.message)
                    }
                    else -> Unit
                }
            }.onFailure {
                stream.value = _stream.error(it)
            }
        }
    }

    //send otp
    fun sendOtp(sendOtpRequest: SendOtpRequest) {
        viewModelScope.launch {
            runCatching {
                sendOtp.value = _sendOto.loading()
                authRepository.sendOtp(sendOtpRequest = sendOtpRequest)
            }.onSuccess {
                sendOtp.value = _sendOto.success(it)
            }.onFailure {
                sendOtp.value = _sendOto.error(it)
            }
        }
    }

    //verifyOtp
    fun verifyOtp(sendOtpRequest: SendOtpRequest) {
        viewModelScope.launch {
            runCatching {
                verifyOtp.value = _verifyOtp.loading()
                authRepository.verifyOtp(sendOtpRequest = sendOtpRequest)
            }.onSuccess {
                verifyOtp.value = _verifyOtp.success(it)
            }.onFailure {
                verifyOtp.value = _verifyOtp.error(it)
            }
        }
    }

    //verifyOtp
    fun changePassword(sendOtpRequest: SendOtpRequest) {
        viewModelScope.launch {
            runCatching {
                changePassword.value = _changePassword.loading()
                authRepository.changePassword(sendOtpRequest = sendOtpRequest)
            }.onSuccess {
                changePassword.value = _changePassword.success(it)
            }.onFailure {
                changePassword.value = _changePassword.error(it)
            }
        }
    }

    //verifyOtp
    fun updateTribes(requestBody: RequestBody) {
        viewModelScope.launch {
            runCatching {
                updateTribes.value = _updateTribes.loading()
                authRepository.updateTribes(requestBody = requestBody)
            }.onSuccess {
                when (it) {
                    is Resource.Success -> {
                        Log.e("User -> ","${it.value.data}")
                        updateTribes.value = _updateTribes.success(data = it.value.data)

                    }
                    is Resource.Failure -> {
                        Log.e("User -> ","${it.message}")
                        updateTribes.value = _updateTribes.error(Throwable(""))

                        handleApiError(it)

                    }
                    else -> Unit
                }
           }.onFailure {
                updateTribes.value = _updateTribes.error(it)
            }
        }
    }
    //UserDetails
    fun getUserDetails(token: String) {
        viewModelScope.launch {
            runCatching {
                authRepository.getUserDetails(token)
            }.onSuccess {
                when (it) {
                    is Resource.Success -> {
                        userDetails.value= _userDetails.success(it.value.data)
                    }
                    is Resource.Failure -> {
                        Log.e("User -> ","${it.message}")
                        handleApiError(it)

                    }
                    else -> Unit
                }
            }.onFailure {
                println(it.message)
            }
        }
    }
}