package com.yol.ui.auth

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.yol.MainActivity
import com.yol.R
import com.yol.adapter.SelectTribesAdapter
import com.yol.ui.SelectTribesActivity
import com.yol.utils.Constants
import com.yol.utils.MyApplication
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_splash_screen)
        checkRunTimePermission()

        //   hash()

    }

    override fun onResume() {
        super.onResume()

    }

    private fun hash() {

        try {
            val info = getPackageManager().getPackageInfo(
                "com.yol",
                PackageManager.GET_SIGNATURES
            );
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }
    }

    private fun requestPermissions() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    Toast.makeText(
                        this@SplashScreen,
                        "Permission Granted",
                        Toast.LENGTH_LONG
                    ).show()
                    navigationToHome()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(
                        this@SplashScreen,
                        "Permission Denied",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    AlertDialog.Builder(this@SplashScreen)
                        .setTitle(com.yol.R.string.permission_rationale_title)
                        .setMessage(R.string.permission_rationale_message)
                        .setNegativeButton(android.R.string.cancel,
                            DialogInterface.OnClickListener { dialog, which ->
                                dialog.dismiss()
                                token!!.cancelPermissionRequest()
                            })
                        .setPositiveButton(android.R.string.ok,
                            DialogInterface.OnClickListener { dialog, which ->
                                dialog.dismiss()
                                token!!.continuePermissionRequest()
                            })
                        .setOnDismissListener(DialogInterface.OnDismissListener { token!!.cancelPermissionRequest() })
                        .show()
                }
            }).check()
    }

    private fun navigationToHome() {
        Handler().postDelayed(Runnable {

            //val user = FirebaseAuth.getInstance().currentUser
            if (MyApplication.tinyDB.getBoolean(Constants.SharedPref.LOGGGED_IN, false)) {
                if (MyApplication.tinyDB.getUserDetails()?.tribes
                        .isNullOrEmpty()
                )
                    startActivity(Intent(this@SplashScreen, SelectTribesActivity::class.java))
                else
                    startActivity(Intent(this@SplashScreen, MainActivity::class.java))

            } else {
                startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
            }

            finish()
        }, 2000)
    }

    private fun checkRunTimePermission() {
        val permissionArrays =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions()
        } else {

            navigationToHome()
        }
    }

    fun getDynamicLink() {
        // App.sessionManager?.setLinkRefferalCode("")
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                deepLink = pendingDynamicLinkData.link

                if (deepLink != null) {
                    /*val refferCode=deepLink.getQueryParameter("referral")
                    if (refferCode!=null)
                        App.sessionManager?.setLinkRefferalCode(refferCode.toString())*/

                }
            }
            .addOnFailureListener(this) { e -> Log.w("re==", "getDynamicLink:onFailure", e) }
    }
}