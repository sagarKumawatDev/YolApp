package com.yol.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.yol.R
import com.yol.databinding.ActivityLoginBinding
import com.yol.utils.ApiState
import com.yol.utils.Constants
import com.yol.utils.MyApplication
import com.yol.utils.ProgressDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.facebook.login.LoginResult
import com.facebook.login.LoginManager
import java.util.*
import com.facebook.GraphRequest
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FacebookAuthProvider
import com.facebook.AccessToken
import com.yol.MainActivity
import com.yol.network.request.LoginRequest
import com.yol.network.request.RegisterRequest
import com.yol.ui.SelectTribesActivity
import com.yol.utils.Constants.SharedPref.Companion.LOGGED_IN_ACCESS_TOKEN
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import java.lang.Thread.sleep
import kotlin.collections.ArrayList


private const val RC_SIGN_IN = 9001

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    private val authViewModel: AuthViewModel by viewModel()
    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(this@LoginActivity);
        AppEventsLogger.activateApp(this);

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_app))
            .requestProfile()
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        mAuth = FirebaseAuth.getInstance();

        init()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                Log.d("Error Message", "firebaseAuthWithGoogle:" + e.message)
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
            super.onActivityResult(requestCode, resultCode, data)

        }
    }

    private fun init() {
        //loginButton = findViewById(R.id.facebookSignIn)
        fcmToken()

        binding.apply {
            rlSingIn.setOnClickListener {
                if (isValid()) {
                    val loginRequest = LoginRequest(
                        email = email.text.toString(),
                        password = password.text.toString(),
                    )
                    authViewModel.login(loginRequest)

                    /*authViewModel.loginByFirebase(
                        email = email.text.toString(),
                        password = password.text.toString()
                    )*/
                }

            }
            //Google Sign In
/*
            googleSignIn.setOnClickListener {
                signIn()
            }
            facebookSignIn.setOnClickListener {

                callbackManager = CallbackManager.Factory.create()
                LoginManager.getInstance().logInWithReadPermissions(
                    this@LoginActivity,
                    Arrays.asList("email", "public_profile", "user_friends")
                );
                LoginManager.getInstance().registerCallback(
                    callbackManager,
                    object : FacebookCallback<LoginResult?> {
                        override fun onSuccess(loginResult: LoginResult?) {

                            val request = GraphRequest.newMeRequest(
                                loginResult?.accessToken
                            ) { data, response ->
                                Log.v("LoginActivity", response.toString())

                                // Application code
                                val email = data.getString("email")
                                val id = data.getString("id")
                                val name = data.getString("name")
                                //val birthday = data.getString("birthday") // 01/31/1980 format
                                Log.d("Email", "" + email)
                                // Log.d("DOB", "" + birthday)

                                loginResult?.accessToken?.let { it1 -> handleFacebookAccessToken(it1) }

                            }
                            val parameters = Bundle()
                            parameters.putString("fields", "id,name,email")
                            request.parameters = parameters
                            request.executeAsync()
                        }

                        override fun onCancel() {
                            Log.d("Success", "FaceBook Token :")
                        }

                        override fun onError(exception: FacebookException) {
                            Log.d("Success", "FaceBook Token : ${exception.message}")

                        }
                    })
            }

*/


            txtSignUp.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            forgotPassword.setOnClickListener {
                startActivity(Intent(this@LoginActivity, OtpActivity::class.java))

            }
        }
        authViewModel.loginState.observe(this, {
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
                                MyApplication.tinyDB.getString(LOGGED_IN_ACCESS_TOKEN)?.let { it1 ->
                                    authViewModel.getUserDetails(
                                        it1
                                    )
                                }
                            }
                            false -> {
//                                Toast.makeText(this, data, Toast.LENGTH_LONG).show()
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
                        finish()
                    }
                }
                ApiState.Status.ERROR -> {
                    ProgressDialog.hideProgressDialog()
                    Toast.makeText(this, it.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    private fun launchActivity(tribes: ArrayList<String>) {
        if (tribes.isNullOrEmpty()
        )
            startActivity(
                Intent(
                    this@LoginActivity,
                    SelectTribesActivity::class.java
                )
            )
        else
            startActivity(
                Intent(
                    this@LoginActivity,
                    MainActivity::class.java
                )
            )

    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mAuth?.currentUser?.let {
                        val registerRequest = RegisterRequest()
                        registerRequest.userId = it.uid
                        registerRequest.name = it.displayName ?: ""
                        registerRequest.email = it.email ?: ""
                        authViewModel.usersReference.child(it.uid).setValue(registerRequest)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    MyApplication.tinyDB.putBoolean(
                                        Constants.SharedPref.LOGGGED_IN,
                                        true
                                    )
                                    launchActivity(ArrayList())
                                    finish()
                                }
                            }
                    }
                } else {
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }?.addOnFailureListener {
                Toast.makeText(
                    this, it.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("TAG", "handleFacebookAccessToken:$token")
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mAuth?.currentUser?.let {
                        val registerRequest = RegisterRequest()
                        registerRequest.userId = it.uid
                        registerRequest.name = it.displayName ?: ""
                        registerRequest.email = it.email ?: ""

                        authViewModel.usersReference.child(it.uid).setValue(registerRequest)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    MyApplication.tinyDB.putBoolean(
                                        Constants.SharedPref.LOGGGED_IN,
                                        true
                                    )
                                    launchActivity(ArrayList())

                                    finish()
                                }
                            }
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }?.addOnFailureListener {
                Toast.makeText(
                    this, it.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun fcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                MyApplication.tinyDB.putString(
                    Constants.SharedPref.USER_TOKEN,
                    task.exception.toString()
                )
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
        })
    }

    private fun isValid(): Boolean {
        binding.apply {
            if (email.text.isNullOrEmpty())
            {
                Toast.makeText(this@LoginActivity,"Please fill email", Toast.LENGTH_LONG).show()
                return false
            }
            else if (password.text.isNullOrEmpty())
            {
                Toast.makeText(this@LoginActivity,"Please password", Toast.LENGTH_LONG).show()
                return false
            }
        }
        return true
    }
}