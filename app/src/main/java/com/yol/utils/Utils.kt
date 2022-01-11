package com.yol.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.yol.di.Resource
import com.yol.ui.auth.LoginActivity
import com.yol.utils.MyApplication.Companion.toast
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject



fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun <A : Activity> Activity.startClearTopActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(it)
    }
}

fun <A : Activity> Activity.startAActivity(activity: Class<A>) {
    Intent(this, activity).also {
        startActivity(it)
    }
}

fun <A : Activity> Activity.startAActivity(activity: Class<A>, data: String) {
    Intent(this, activity).putExtra("data", data).also {
        startActivity(it)
    }
}


fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

fun snackbar(message: String, context: View, action: (() -> Unit)? = null) {
    var mess = message
    if (message.isNullOrEmpty()) mess = "Bad Request"
    val snackbar = Snackbar.make(context, mess, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}


fun handleApiError(
    failure: Resource.Failure,
) {
    when {
        failure.isNetworkError -> {
            toast("Please check your internet connection")
        }
        failure.errorCode == 400 -> {
            try {
                val jsonObject = JSONObject(failure.message)
                val error = jsonObject.getString("message")
                toast(error)

            } catch (err: JSONException) {
                Log.d("Error", err.toString())
                val error = "Bad Request"
                toast(error)
                // snackbar(error, view)
            }

        }
        failure.errorCode == 500 -> {
          toast("Need to login again")
            MyApplication.tinyDB.clear()
            //activity?.startNewActivity(LoginActivity::class.java)
        }
        else -> {
            try {
                val jsonObject = JSONObject(failure.message)
                val error = jsonObject.getString("message")
                toast(error)

            } catch (err: JSONException) {
                Log.d("Error", err.toString())
                val error = "Bad Request"
                toast(error)
                // snackbar(error, view)
            }

           // snackbar(error, view)
        }
    }
}

fun handleApiError(
    failure: Resource.Failure,
    message: String = "",
    view: View,
    activity: Activity? = null,
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> snackbar(
            "Please check your internet connection", view,
            retry
        )
        failure.errorCode == 400 -> {
            activity?.let {
                Helper.hideKeyboard(it)
            }
            try {
                if (message != "") {
                    val jsonObject = JSONObject(message)
                    val error = jsonObject.getString("Message")
                    if (error == "One or more validation failures have occurred.") {
                        var errorArr: JSONArray = jsonObject.getJSONArray("Errors")
                        snackbar(errorArr.get(0).toString(), view)
                    } else
                        snackbar(error, view)
                } else {
                    val jsonObject = JSONObject(failure.errorBody!!.string().toString())
                    val error = jsonObject.getString("Message")
                    if (error == "One or more validation failures have occurred.") {
                        var errorArr: JSONArray = jsonObject.getJSONArray("Errors")
                        snackbar(errorArr.get(0).toString(), view)
                    } else
                        snackbar(error, view)

                }


            } catch (err: JSONException) {
                Log.d("Error", err.toString())
                val error = "Bad Request"
                snackbar(error, view)
            }

        }
        failure.errorCode == 500 -> {
          toast("Need to login again")
            MyApplication.tinyDB.clear()
            activity?.startNewActivity(LoginActivity::class.java)
        }
        else -> {
            try {
                if (message != "") {
                    val jsonObject = JSONObject(message)
                    val error = jsonObject.getString("Message")
                    if (error == "One or more validation failures have occurred.") {
                        var errorArr: JSONArray = jsonObject.getJSONArray("Errors")
                        snackbar(errorArr.get(0).toString(), view)
                    } else
                        snackbar(error, view)
                } else {
                    val jsonObject = JSONObject(failure.errorBody!!.string().toString())
                    val error = jsonObject.getString("Message")
                    if (error == "One or more validation failures have occurred.") {
                        var errorArr: JSONArray = jsonObject.getJSONArray("Errors")
                        snackbar(errorArr.get(0).toString(), view)
                    } else
                        snackbar(error, view)
                }


            } catch (err: JSONException) {
                Log.d("Error", err.toString())
                val error = "Bad Request"
                snackbar(error, view)
            }

        }
    }
}

